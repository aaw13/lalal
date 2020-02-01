package com.example.janet.models;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Janet {
    private String response = "Hi! I'm Janet. How can I help you?";
    private CommandTypesEnum nextCommandType = CommandTypesEnum.NONE_ACTION;
    private Context context;
    private PackageManager pm;
    private String[] arguments = new String[2];

    public Janet(Context context, PackageManager pm)
    {
        this.context = context;
        this.pm = pm;
    }

    public void ProcessCommand(String command)
    {
        CommandTypesEnum commandType = CommandTypesEnum.NONE_ACTION;
        if(command.isEmpty())
        {
            response = "I'm not good at guessing.";
            return;
        }

        List<String> preprocessedCommand = preprocessCommand(command);

        if(checkGreetings(preprocessedCommand))
            commandType = CommandTypesEnum.TEXT_ACTION;

        if(checkBrowser(preprocessedCommand, command))
            commandType = CommandTypesEnum.BROWSE_INTERNET;

        if(checkApplication(preprocessedCommand))
            commandType = CommandTypesEnum.RUN_APPLICATION;

        if(commandType == CommandTypesEnum.NONE_ACTION && response=="") {
            response = "I can't understand you.";
        }
        nextCommandType = commandType;
    }

    public String getResponse()
    {
        return response;
    }

    public void makeAction() {
        if(nextCommandType.equals(CommandTypesEnum.RUN_APPLICATION))
        {
            Intent intent = new Intent();
            intent.setClassName(arguments[0],
                    arguments[1]);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        if(nextCommandType.equals(CommandTypesEnum.BROWSE_INTERNET))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + arguments[0]));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        nextCommandType = CommandTypesEnum.NONE_ACTION;
        response = "";
    }

    private List<String> preprocessCommand(String command)
    {
        command = command.toLowerCase();
        command = command.replaceAll("[^a-z\\s]","");
        String[] commandArray = command.split("\\s");
        List<String> resultList = new ArrayList<String>();
        for(String commandWord: commandArray)
        {
            Stemmer stemmer = new Stemmer();
            stemmer.add(commandWord.toCharArray(), commandWord.length());
            stemmer.stem();
            String resultWord = stemmer.toString();
            resultList.add(resultWord);
        }
        return resultList;
    }

    private boolean checkGreetings(List<String> preprocessedCommand)
    {
        for(String keyWord: Keywords.Greetings())
        {
            if (preprocessedCommand.contains(keyWord))
            {
                response = "Hello, what can I do for you?";
                return true;
            }
        }
        return false;
    }

    private boolean checkApplication(List<String> preprocessedCommand)
    {
        for(String keyWord: Keywords.RunApplication())
        {
            for(int i=0;i<preprocessedCommand.size()-1;i++)
            {
                String word = preprocessedCommand.get(i);
                if(word.equals(keyWord)) {
                    if(i>0)
                    {
                        for (String negative : Keywords.NegationWords()) {
                            if (preprocessedCommand.get(i - 1).equals(negative))
                            {
                                response = "Ok, i won't do that.";
                                return false;
                            }
                        }
                    }
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
                    response = "I haven't found any matching application.";
                    for (ResolveInfo info : pkgAppsList) {
                        String appName = info.loadLabel(pm).toString().toLowerCase();
                        Stemmer stemmer = new Stemmer();
                        stemmer.add(appName.toCharArray(), appName.length());
                        stemmer.stem();
                        String resultAppName = stemmer.toString();
                        if (preprocessedCommand.contains(resultAppName)) {
                            arguments[0] = info.activityInfo.applicationInfo.packageName;
                            arguments[1] = info.activityInfo.name;
                            response = "Starting application: " + appName;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkBrowser(List<String> preprocessedCommand, String command)
    {
        for(String keyWord: Keywords.RunBrowser())
        {
            for(int i=0;i<preprocessedCommand.size()-1;i++)
            {
                String word = preprocessedCommand.get(i);
                if(word.equals(keyWord))
                {
                    if(i>0) {
                        for (String negative : Keywords.NegationWords()) {
                            if (preprocessedCommand.get(i - 1).equals(negative))
                            {
                                response = "Ok, i won't do that.";
                                return false;
                            }
                        }
                    }
                    if(preprocessedCommand.get(i+1).equals("me") || preprocessedCommand.get(i+1).equals("for"))
                    {
                        command = command.replaceFirst(preprocessedCommand.get(i+1), "");
                    }
                    arguments[0] = command.substring(command.indexOf(keyWord) + keyWord.length());
                    response = "Searching for: " + arguments[0];
                    return true;
                }
            }
        }
        return false;
    }
}
