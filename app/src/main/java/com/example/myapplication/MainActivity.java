package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.AgendaModel;
import com.example.myapplication.model.NewsModel;
import com.example.myapplication.model.ResourceModel;
import com.example.myapplication.model.SignInPayload;
import com.example.myapplication.model.SignUpPayload;
import com.example.myapplication.model.AuthResponse;
import com.example.myapplication.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private List<ResourceModel> activeResources;
    private List<AgendaModel> activeAgendas;
    private List<NewsModel> activeNews;
    private String jwtToken;
    private UserModel loggedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activeResources = fetchActiveResources();
        activeAgendas = fetchActiveAgendas();
        activeNews = fetchActiveNews();
        tryToLogin();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        registerEvents();


    }

    void tryToLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        loggedUser = fetchLoggedUser();
    }

    public void register(View view) {
        jwtToken = null;
        loggedUser = null;

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwtToken");
        editor.apply();

        TextView registerErrTextView = (TextView)findViewById(R.id.registerErrTextView);
        EditText usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        EditText pwdEditText = (EditText)findViewById(R.id.pwdEditText);
        EditText pwdConfirmEditText = (EditText)findViewById(R.id.pwdConfirmEditText);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);

        registerBtn.setEnabled(false);

        registerErrTextView.setVisibility(View.GONE);

        String username = usernameEditText.getText().toString();
        String password = pwdEditText.getText().toString();
        String passwordConfirmation = pwdConfirmEditText.getText().toString();

        if (username.length() < 4 || username.length() >= 20) {
            registerErrTextView.setText("Username should be longer than 3 chars and shorter than 20!");
            registerErrTextView.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(true);
            return;
        }

        if (password.length() < 8 || password.length() >= 50) {
            registerErrTextView.setText("Password should be longer than 8 chars and shorter than 50!");
            registerErrTextView.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(true);
            return;
        }

        if (!password.equals(passwordConfirmation)) {
            registerErrTextView.setText("Passwords do not match!");
            registerErrTextView.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(true);
            return;
        }

        SignUpPayload signUpPayload = new SignUpPayload();
        signUpPayload.setUsername(username);
        signUpPayload.setPassword(password);

        CompletableFuture<AuthResponse> future = HttpService.signUpAsync(signUpPayload)
                .thenApply(response -> {
                    runOnUiThread(() -> {
                        jwtToken = response.getAccessToken();
                        loggedUser = fetchLoggedUser();

                        editor.putString("jwtToken", jwtToken);
                        editor.apply();

                        registerBtn.setEnabled(true);
                        go_to_user_panel();
                    });
                    return response;
                })
                .exceptionally(ex -> {
                    // Handle exceptions (e.g., network failure)
                    if (ex.getCause() != null) {
                        String errorMessage = ex.getCause().getMessage();
                        runOnUiThread(() -> {
                            registerErrTextView.setText(errorMessage);
                            registerErrTextView.setVisibility(View.VISIBLE);
                            registerBtn.setEnabled(true);
                        });
                    }
                    //ex.printStackTrace();
                    return null;
                });
    }

    public void login(View view) {
        jwtToken = null;
        loggedUser = null;

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwtToken");
        editor.apply();

        TextView loginErrTextView = (TextView)findViewById(R.id.loginErrTextView);
        EditText usernameEditText = (EditText)findViewById(R.id.lUsernameEditText);
        EditText pwdEditText = (EditText)findViewById(R.id.lPwdEditText);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);

        loginBtn.setEnabled(false);

        loginErrTextView.setVisibility(View.GONE);

        String username = usernameEditText.getText().toString();
        String password = pwdEditText.getText().toString();

        if (username.length() < 4 || username.length() >= 20) {
            loginErrTextView.setText("Username should be longer than 3 chars and shorter than 20!");
            loginErrTextView.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(true);
            return;
        }

        if (password.length() < 8 || password.length() >= 50) {
            loginErrTextView.setText("Password should be longer than 8 chars and shorter than 50!");
            loginErrTextView.setVisibility(View.VISIBLE);
            loginBtn.setEnabled(true);
            return;
        }

        SignInPayload signInPayload = new SignInPayload();
        signInPayload.setUsername(username);
        signInPayload.setPassword(password);

        CompletableFuture<AuthResponse> future = HttpService.signInAsync(signInPayload)
                .thenApply(response -> {
                    runOnUiThread(() -> {
                        jwtToken = response.getAccessToken();

                        editor.putString("jwtToken", jwtToken);
                        editor.apply();

                        loginBtn.setEnabled(true);

                        loggedUser = fetchLoggedUser();

                        go_to_user_panel();
                    });
                    return response;
                })
                .exceptionally(ex -> {
                    // Handle exceptions (e.g., network failure)
                    if (ex.getCause() != null) {
                        String errorMessage = ex.getCause().getMessage();
                        runOnUiThread(() -> {
                            loginErrTextView.setText(errorMessage);
                            loginErrTextView.setVisibility(View.VISIBLE);
                            loginBtn.setEnabled(true);
                        });
                    }
                    //ex.printStackTrace();
                    return null;
                });

        //4-20 8-50
    }

    UserModel fetchLoggedUser() {
        CompletableFuture<UserModel> future = HttpService.getMeAsync(jwtToken)
                .thenApply(response -> {
                    return response;
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        return future.join();
    }

    private void displayResources(List<ResourceModel> resourceList) {
        LinearLayout resourcesWrapper = findViewById(R.id.resourcesWrapper);

        for (ResourceModel resource : resourceList) {
            // Create a new child LinearLayout for each resource
            LinearLayout resourceLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams resourceLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            resourceLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10)); // Left, Top, Right, Bottom
            resourceLayout.setLayoutParams(resourceLayoutParams);
            resourceLayout.setOrientation(LinearLayout.VERTICAL);
            resourceLayout.setBackgroundColor(Color.parseColor("#FACAB9"));
            resourcesWrapper.addView(resourceLayout);

// Create the title bar for the resource
            LinearLayout titleBarLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams titleBarLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(60));
            titleBarLayoutParams.setMargins(0, 0, 0, dpToPx(10)); // Bottom margin
            titleBarLayout.setLayoutParams(titleBarLayoutParams);
            titleBarLayout.setBackgroundColor(Color.parseColor("#363434"));
            titleBarLayout.setOrientation(LinearLayout.VERTICAL);
            resourceLayout.addView(titleBarLayout);

// Create and add the title TextView
            TextView titleTextView = new TextView(MainActivity.this);
            LinearLayout.LayoutParams titleTextLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleTextLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            titleTextView.setLayoutParams(titleTextLayoutParams);
            titleTextView.setGravity(Gravity.CENTER);
            titleTextView.setText(resource.getName());
            titleTextView.setTextColor(Color.WHITE);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            titleBarLayout.addView(titleTextView);

            TextView addressTextView = new TextView(MainActivity.this);
            addressTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            addressTextView.setPadding(dpToPx(10), 10, 0, 0);
            addressTextView.setText("Address: " + resource.getAddress());
            addressTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            TextView attendingHoursTextView = new TextView(MainActivity.this);
            attendingHoursTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            attendingHoursTextView.setPadding(dpToPx(10), 0, 0, 0);
            attendingHoursTextView.setText("Attending hours: " + resource.getAttendingHours());
            attendingHoursTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            TextView descriptionTextView = new TextView(MainActivity.this);
            descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            descriptionTextView.setPadding(dpToPx(10), 10, dpToPx(10), dpToPx(10));
            descriptionTextView.setText(resource.getDescription());
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            resourceLayout.addView(addressTextView);
            resourceLayout.addView(attendingHoursTextView);
            resourceLayout.addView(descriptionTextView);
        }
    }

    private void displayAgendas(List<AgendaModel> agendaList) {
        LinearLayout agendasWrapper = findViewById(R.id.agendasWrapper);

        for (AgendaModel agenda : agendaList) {
            // Create a new child LinearLayout for each agenda
            LinearLayout agendaLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams agendaLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            agendaLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10)); // Left, Top, Right, Bottom
            agendaLayout.setLayoutParams(agendaLayoutParams);
            agendaLayout.setOrientation(LinearLayout.VERTICAL);
            agendaLayout.setBackgroundColor(Color.parseColor("#FACAB9"));
            agendasWrapper.addView(agendaLayout);

// Create the title bar for the agenda
            LinearLayout titleBarLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams titleBarLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(60));
            titleBarLayoutParams.setMargins(0, 0, 0, dpToPx(10)); // Bottom margin
            titleBarLayout.setLayoutParams(titleBarLayoutParams);
            titleBarLayout.setBackgroundColor(Color.parseColor("#363434"));
            titleBarLayout.setOrientation(LinearLayout.VERTICAL);
            agendaLayout.addView(titleBarLayout);

// Create and add the title TextView
            TextView titleTextView = new TextView(MainActivity.this);
            LinearLayout.LayoutParams titleTextLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleTextLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            titleTextView.setLayoutParams(titleTextLayoutParams);
            titleTextView.setGravity(Gravity.CENTER);
            titleTextView.setText(agenda.getActivity());
            titleTextView.setTextColor(Color.WHITE);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            titleBarLayout.addView(titleTextView);

            TextView addressTextView = new TextView(MainActivity.this);
            addressTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            addressTextView.setPadding(dpToPx(10), 10, 0, 0);
            addressTextView.setText("Address: " + agenda.getAddress());
            addressTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            TextView dateTextView = new TextView(MainActivity.this);
            dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.mm");

            dateTextView.setPadding(dpToPx(10), 0, 0, 0);
            dateTextView.setText("Date: " + dateFormat.format(agenda.getDate()));
            dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            TextView descriptionTextView = new TextView(MainActivity.this);
            descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            descriptionTextView.setPadding(dpToPx(10), 10, dpToPx(10), dpToPx(10));
            descriptionTextView.setText(agenda.getDescription());
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            agendaLayout.addView(addressTextView);
            agendaLayout.addView(dateTextView);
            agendaLayout.addView(descriptionTextView);
        }
    }

    private void displayNews(List<NewsModel> newsList) {
        LinearLayout newsWrapper = findViewById(R.id.newsWrapper);

        for (NewsModel news : newsList) {
            // Create a new child LinearLayout for each news
            LinearLayout newsLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams newsLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            newsLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10)); // Left, Top, Right, Bottom
            newsLayout.setLayoutParams(newsLayoutParams);
            newsLayout.setOrientation(LinearLayout.VERTICAL);
            newsLayout.setBackgroundColor(Color.parseColor("#FACAB9"));
            newsWrapper.addView(newsLayout);

// Create the title bar for the news
            LinearLayout titleBarLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams titleBarLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(60));
            titleBarLayoutParams.setMargins(0, 0, 0, dpToPx(10)); // Bottom margin
            titleBarLayout.setLayoutParams(titleBarLayoutParams);
            titleBarLayout.setBackgroundColor(Color.parseColor("#363434"));
            titleBarLayout.setOrientation(LinearLayout.VERTICAL);
            newsLayout.addView(titleBarLayout);

// Create and add the title TextView
            TextView titleTextView = new TextView(MainActivity.this);
            LinearLayout.LayoutParams titleTextLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleTextLayoutParams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            titleTextView.setLayoutParams(titleTextLayoutParams);
            titleTextView.setGravity(Gravity.CENTER);
            titleTextView.setText(news.getTitle());
            titleTextView.setTextColor(Color.WHITE);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            titleBarLayout.addView(titleTextView);

            TextView descriptionTextView = new TextView(MainActivity.this);
            descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            descriptionTextView.setPadding(dpToPx(10), 10, dpToPx(10), dpToPx(10));
            descriptionTextView.setText(news.getContent());
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

            newsLayout.addView(descriptionTextView);
        }
    }

    private List<ResourceModel> fetchActiveResources() {
        CompletableFuture<List<ResourceModel>> future = HttpService.getActiveResourcesAsync()
                .thenApply(resourceList -> {
                    return resourceList;
                })
                .exceptionally(ex -> {
                    // Handle exceptions (e.g., network failure)
                    ex.printStackTrace();
                    return Collections.emptyList();
                });

        return future.join();
    }

    private List<AgendaModel> fetchActiveAgendas() {
        CompletableFuture<List<AgendaModel>> future = HttpService.getActiveAgendasAsync()
                .thenApply(agendaList -> {
                    return agendaList;
                })
                .exceptionally(ex -> {
                    // Handle exceptions (e.g., network failure)
                    ex.printStackTrace();
                    return Collections.emptyList();
                });

        return future.join();
    }

    private List<NewsModel> fetchActiveNews() {
        CompletableFuture<List<NewsModel>> future = HttpService.getActiveNewsAsync()
                .thenApply(newsList -> {
                    return newsList;
                })
                .exceptionally(ex -> {
                    // Handle exceptions (e.g., network failure)
                    ex.printStackTrace();
                    return Collections.emptyList();
                });

        return future.join();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void addTextView(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        layout.addView(textView);
    }


    private void registerEvents() {
        LinearLayout l1=(LinearLayout) findViewById(R.id.l1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.resources);
                displayResources(activeResources);
            }
        });
        LinearLayout l2=(LinearLayout) findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.agenda);
                displayAgendas(activeAgendas);
            }
        });
        LinearLayout l3=(LinearLayout) findViewById(R.id.l3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.layout);
                displayNews(activeNews);
            }
        });
        ImageView i1=(ImageView) findViewById(R.id.imageView);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                go_to_user_panel();
            }
        });


    }
    public void back_to_home_page(View view)
    {
        setContentView(R.layout.activity_main);
        registerEvents();
    }

    public void go_to_register(View view)
    {
        setContentView(R.layout.register);
        //registerEvents();
    }

    public void go_to_login(View view)
    {
        setContentView(R.layout.login);
        //registerEvents();
    }

    public void go_to_user_panel() {
        if (loggedUser == null) {
            setContentView(R.layout.login);
            return;
        }
        setContentView(R.layout.acc_info);


        TextView usernameTextView = (TextView)findViewById(R.id.usernameTextView);
        TextView createdAtTextView = (TextView)findViewById(R.id.createdAtTextView);
        TextView roleTextView = (TextView)findViewById(R.id.roleTextView);
        Button gotoAdminBtn = (Button)findViewById(R.id.gotoAdminBtn);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        gotoAdminBtn.setVisibility(View.GONE);
        usernameTextView.setText("Username: " + loggedUser.getUsername());
        createdAtTextView.setText("Account created at: " + dateFormat.format(loggedUser.getCreatedAt()));
        roleTextView.setText("Role: " + loggedUser.getRole());

        if (loggedUser.getRole().equals("CONTENT_MANAGER") ||
            loggedUser.getRole().equals("ADMINISTRATOR") ||
            loggedUser.getRole().equals("SUPER_ADMINISTRATOR")) {
            gotoAdminBtn.setVisibility(View.VISIBLE);
        }
    }

    public void logout(View view) {
        loggedUser = null;
        jwtToken = null;

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwtToken");
        editor.apply();

        back_to_home_page(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return false;
    }
}