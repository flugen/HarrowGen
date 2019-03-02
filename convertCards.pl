#! perl

use Carp;
use strict;

open(my $IN,"<","harrowlist.txt") or croak("Couldn't open input file");
open(my $OUT,">","harrowcode.txt") or croak("Couldn't open input file") and close($IN);

my $cnum = 0;
while (my $line = <$IN>){
	
	$line =~ /(?<name>[^\t]+)\t(?<card>\w+)\t(?<align>\w+)\t(?<desc>[^\t]+)\t(?<suit>\w+)\t(?<action>.*)/;
	# deck[0] = new HarrowCard("The Avalanche", "QC", Alignment.LE, "desc", Suit.key, null, null);
	my $outline = "deck\[${\($cnum++)}\] = new HarrowCard(\"$+{name}\",\"$+{card}\", Alignment.${\(uc($+{align}))},\"$+{desc}\",Suit.${\(lc($+{suit}))},null,\"$+{action}\");\n";
	$outline =~ s/\.N,/.NN,/;
	print($OUT $outline);
}

close $IN;
close $OUT;
