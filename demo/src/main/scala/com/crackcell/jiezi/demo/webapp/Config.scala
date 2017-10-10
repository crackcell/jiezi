package com.crackcell.jiezi.demo.webapp

/**
  * 配置类
  *
  * 命令行参数。
  *
  * @author Menglong TAN
  */
case class Config(port: Int = 8080, dicts: Seq[String] = Seq())

object Config {
  private val parser = new scopt.OptionParser[Config]("jiezi-demo") {
    head("jiezi-demo", "1.0")

    help("help").text("print this usage text")

    opt[Seq[String]]('d', "dicts").valueName("<dict1>,<dict2>...").action( (x,c) => c.copy(dicts = x) ).text("dicts")
    opt[Int]('p', "port").action((x, c) => c.copy(port = x)).text("port")
  }

  def parse(args: Array[String]): Config = {
    parser.parse(args, Config()) match {
      case Some(config) =>
        config
      case None =>
        parser.showTryHelp
        throw new Exception("wrong arguments")
    }
  }
}

