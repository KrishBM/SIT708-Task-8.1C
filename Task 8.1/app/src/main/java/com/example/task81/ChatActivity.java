package com.example.task81;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private ImageButton sendButton;

    private String username;
    private RequestQueue requestQueue;
    private String chatbotUrl = "http://192.168.0.100:5000/chat"; // Using 10.0.2.2 to access localhost from Android emulator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get username from intent
        username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            username = "User";
        }

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Set up RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Add welcome message
        addBotMessage("Welcome " + username + "!");

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        // Add user message to the chat
        addUserMessage(messageText);

        // Clear input field
        messageInput.setText("");

        // Send message to the chatbot API
        sendToChatbot(messageText);
    }

    private void addUserMessage(String message) {
        messageList.add(new Message(message, Message.TYPE_USER));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        messageList.add(new Message(message, Message.TYPE_BOT));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void sendToChatbot(final String userMessage) {
        // Create a POST request to the chatbot API
        StringRequest stringRequest = new StringRequest(Request.Method.POST, chatbotUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Add bot response to the chat
                        addBotMessage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        addBotMessage("Sorry, I couldn't process your request. Please try again.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userMessage", userMessage);
                return params;
            }
        };

        // Add request to queue
        requestQueue.add(stringRequest);
    }
} 