package com.crackcell.jiezi.demo.webapp

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.crackcell.jiezi.dict.parser.{FileToStream, TermDictLoader}
import com.crackcell.jiezi.segment.{ForwardMMSegment, Segment}
import org.apache.commons.logging.LogFactory
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Request, Server}

/**
  * 用于展示用的web服务器
  *
  * @author Menglong TAN
  */
object Main {

  private lazy val logger = LogFactory.getLog("Demo")

  private lazy val template =
    """
      |<html>
      |  <head>
      |    <title>JieZi Wordseg Demo</title>
      |    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
      |    <meta name="viewport" content="height=device-height width=device-width">
      |  </head>
      |  <body>
      |    <p>
      |      <form action="/wordseg" method="get">
      |        <p>query: </p>
      |        <p><textarea name="query" rows="10" cols="40"></textarea></p>
      |        <input type="submit" value="split!" />
      |      </form>
      |    </p>
      |    <p>Results:</p>
      |    <p>##result##</p>
      |    <p>##badcase##</p>
      |  </body>
      |</html>
    """.stripMargin

  def main(args: Array[String]): Unit = {
    val config = Config.parse(args)

    val dictPaths: Array[String] =
      if (config.dicts.length == 0)
        Array(Main.getClass.getClassLoader.getResource("").getPath + "../../../dist/dict/")
      else
        config.dicts.toArray

    val loader = new TermDictLoader(new FileToStream)
    val dicts = dictPaths.map { path =>
      logger.info(s"loading dict: ${path}")
      loader.loadDict(path)
    }
    logger.info("finish loading dicts")
    val segment = new ForwardMMSegment(dicts: _*).setHandleInvalid("skip")

    val server = new Server(config.port)
    server.setHandler(new IndexHandler(segment))
    logger.info(s"starting server on port ${config.port}")
    server.start()
    server.join()
  }

  class IndexHandler(val segment: Segment) extends AbstractHandler {
    override def handle(s: String, request: Request, servletRequest: HttpServletRequest,
                        response: HttpServletResponse): Unit = {
      var resp = template

      val query = servletRequest.getParameter("query")
      if (query != null) {
        val terms = segment.parse(query).terms
        val content = terms.mkString(" <br/>\n")
        logger.info(s"query: ${query}")
        resp = resp.replace("##result##", content)
          .replace("##badcase##",
            s"""
              |<a href="mailto:me@crackcell.com?subject=jiezi-badcase&body=${query}">报badcase</a>
            """.stripMargin)
          .replace("</textarea>", s"${query}</textarea>")
      }

      response.setContentType("text/html;charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)
      request.setHandled(true)
      response.getWriter.println(resp)
    }
  }

}
