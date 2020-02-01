package com.example.janet;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.janet.models.MessageTypesEnum;
import com.example.janet.models.Janet;
import com.example.janet.models.Message;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    Janet janet;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        janet = new Janet(getApplicationContext(), getPackageManager());
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
        tts.setLanguage(Locale.US);
        Message welcomeMessage = new Message(janet.getResponse(), MessageTypesEnum.JANET);
        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText userMessage = (EditText) findViewById(R.id.input_message);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = userMessage.getText().toString();
                userMessage.setText("");
                sendMessage(message);
            }
        });
        ImageButton micButton = (ImageButton) findViewById(R.id.mic_button);
        micButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getVoiceInput();
            }
        });
        displayMessage(welcomeMessage);
    }

    protected void sendMessage(String message)
    {
        Message userMessage = new Message(message, MessageTypesEnum.USER);
        displayMessage(userMessage);
        GenerateJanetResponse(message);
    }

    protected void GenerateJanetResponse(String command)
    {
        janet.ProcessCommand(command);
        String response = janet.getResponse();
        Message janetMessage = new Message(response, MessageTypesEnum.JANET);
        displayMessage(janetMessage);

        makeAction();
    }

    protected  void displayMessage(Message message)
    {
        View messageView;
        LinearLayout messageList = (LinearLayout) findViewById(R.id.messages_list);
        if(message.getSender() == MessageTypesEnum.USER) {
            messageView = getLayoutInflater().inflate(R.layout.user_message, null);
        }
        else
        {
            messageView = getLayoutInflater().inflate(R.layout.bot_message, null);
            tts.speak(message.getMessage(), TextToSpeech.QUEUE_FLUSH, null,null);
        }
        TextView textView = messageView.findViewById(R.id.message_view);
        textView.setText(message.getMessage());
        messageList.addView(messageView,0);
    }

    protected  void makeAction()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                janet.makeAction();
            }
        }, 3000);
    }

    private void getVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sendMessage(result.get(0));
                }
                break;
            }

        }
    }
}
