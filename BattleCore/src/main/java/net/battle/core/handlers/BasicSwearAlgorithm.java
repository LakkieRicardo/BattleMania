package net.battle.core.handlers;

import java.util.Arrays;
import java.util.List;

public class BasicSwearAlgorithm extends SwearSearchAlgorithm {

    public BasicSwearAlgorithm() {
        super("ssa-1");
    }

    public static final List<String> SWEARS = Arrays.asList("anal,anus,arse,ass,ballsack,balls,bastard,bitch,biatch,bloody,blowjob,bollock,bollok,boner,boob,bugger,bum,butt,buttplug,clitoris,coc,coon,crap,cunt,damn,dick,dildo,dyke,fag,feck,fellate,fellatio,felching,fuck,fudgepacker,flange,Goddamn,hell,homo,jerk,jizz,knobend,labia,lmao,lmfao,muff,nigger,nigga,omg,penis,piss,poop,prick,pube,pussy,queer,scrotum,sex,shit,sh1t,slut,smegma,spunk,tit,tosser,turd,twat,vagina,wank,whore".split(","));

    public boolean isSwearWord(String word) {
        return SWEARS.contains(word.toLowerCase());
    }

}