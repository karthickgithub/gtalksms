package com.googlecode.gtalksms.cmd;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;

import com.googlecode.gtalksms.R;
import com.googlecode.gtalksms.tools.StringFmt;
import com.googlecode.gtalksms.xmpp.XmppMsg;

public class Cmd {
    public class SubCmd {
        private String mName;
        private String mHelp;
        private String [] mAlias;
        
        SubCmd(String name, Cmd baseCmd, int resHelp, String args, Object... alias) {
            mName = name;
            mAlias = Arrays.copyOf(alias, alias.length, String[].class);
            
            mHelp = buildHelp(baseCmd, mName, mAlias, resHelp, args);
        }
        
        public String getName() {
            return mName;
        }
        
        public String getHelp() {
            return mHelp;
        }
        
        public String[] getAlias() {
            return mAlias;
        }
    }
    
    private String mName;
    private int mResHelp;
    private String mHelpArgs;
    private String [] mAlias;
    private ArrayList<SubCmd> mSubCmds;
    private static Context sContext;
    
    Cmd(String name, Object... alias) {
        mName = name;
        mAlias = Arrays.copyOf(alias, alias.length, String[].class);
        mSubCmds = new ArrayList<SubCmd>();
    }
    
    public void AddSubCmd(String name, int resHelp, String args, Object... alias) {
        mSubCmds.add(new SubCmd(name, this, resHelp, args, alias));
    }
    
    public static void setContext(Context c) {
        sContext = c;
    }
    
    protected static String makeBold(String msg) {
        return XmppMsg.makeBold(msg);
    }
    
    protected static String getString(int id, Object... args) {
        return sContext.getString(id, args);
    }
    
    protected static String buildHelp(Cmd root, String name, String [] alias, int resHelp, String args ) {
        if (resHelp <= 0) {
            return null;
        }
        
        ArrayList<String> cmds = new ArrayList<String>();

        if (root != null) {
            ArrayList<String> roots = new ArrayList<String>();
            roots.add(root.mName);
            roots.addAll(Arrays.asList(root.mAlias));

            for (String r : roots) {
                ArrayList<String> cur = new ArrayList<String>();
                cur.add(name);
                cur.addAll(Arrays.asList(alias));
                for (String c : cur) {
                    cmds.add("\"" + r + ":" + c + (args == null ? "" : ":" + args) + "\"");
                }
            }
        } else {
            cmds.add(name);
            cmds.addAll(Arrays.asList(alias));
            for (int i = 0 ; i < cmds.size() ; ++i) {
                cmds.set(i, "\"" + cmds.get(i) + (args == null ? "" : ":" + args) + "\"");
            }
        }
        return "- " + StringFmt.join(cmds, getString(R.string.or), true) + " : " + getString(resHelp);
    }
    
    public void setHelp(int resHelp, String args) {
        mResHelp = resHelp;
        mHelpArgs = args;
    }
    
    public String getName() {
        return mName;
    }
    
    public String getHelp() {
        return buildHelp(null, mName, mAlias, mResHelp, mHelpArgs);
    }
    
    public String getHelpSummary() {
        return mResHelp <= 0 ? "" : getString(mResHelp);
    }
    
    public String[] getAlias() {
        return mAlias;
    }    
 
    public ArrayList<SubCmd> getSubCmds() {
        return mSubCmds;
    }
}