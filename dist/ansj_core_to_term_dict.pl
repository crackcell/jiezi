#!/usr/bin/perl
##! @description: usage
##! @version: 1
##! @author: crackcell <tanmenglong@gmail.com>
##! @date:   Mon Oct  9 16:27:19 2017

use strict;

#--------------- global variable --------------


#------------------ function ------------------


#------------------- main -------------------

while (<>) {
    chomp;
    $_ = lc($_);
    my @tokens = split(/\t/, $_);
    if (scalar @tokens != 6) {
        next
    }
    my $word = $tokens[1];
    $tokens[5] =~ /{(.*?)}/;
    my @ns = split(/, /, $1);
    my @nnn = ();
    foreach my $n (@ns) {
        my @ss = split(/=/, $n);
        push(@nnn, $ss[0])
    }

    my $freq = $tokens[2];
    print($word . "\t" . join(",", @nnn) . "\t" . $freq . "\n")
}
