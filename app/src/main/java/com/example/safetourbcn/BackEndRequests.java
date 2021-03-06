package com.example.safetourbcn;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackEndRequests {
    private static BackEndRequests ber;

    private String serverAddress = "http://10.4.41.144:3000";

    private OkHttpClient client;
    private JSONArray usersList;
    private JSONArray placesList;
    private String errorMsg;


    private BackEndRequests() {
        client = new OkHttpClient();
        errorMsg = "connection";

        updateUsersList();
    }

    public static BackEndRequests getInstance() {
        if(ber == null) {
            ber = new BackEndRequests();
        }

        return ber;
    }




    /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////USERS//////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////


    public void login(String user, String pwd) {
        String url = serverAddress + "/user/login";
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject userJSON = new JSONObject();

        try {
            userJSON.put("email", user);
            userJSON.put("password", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(userJSON.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    System.out.println("body: " + response.body().string());
                }
            }

        });
    }






    public void updateUsersList() {
        String url = serverAddress + "/users";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        usersList = new JSONArray(r);
                        errorMsg = "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMsg = "connection";
                    }
                }
            }

        });
    }


    public JSONArray getUsersList() { return usersList;}



    public void addUser(String user, String pwd, String name) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newUser = new JSONObject();
        String url = "http://10.4.41.144:3000/registerIndividualUser";

        try {
            newUser.put("email", user);
            newUser.put("username", name);
            newUser.put("password", pwd);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newUser.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body).
                build();
        System.out.println(" ");
        System.out.println("user: " + user);
        System.out.println("pwd: " + pwd);
        System.out.println(" ");

        try {
            Response response = client.newCall(request).execute();
            updateUsersList();
        } catch (IOException e) {
            System.out.println("ERROR//////////////////////////////////////////7");
            e.printStackTrace();
        }
    }

    public void editUser(String user, String name) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newName = new JSONObject();
        String url = "http://10.4.41.144:3000/users/name/"+user;

        try {
            newName.put("username", name);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newName.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body).
                 build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String TAG = "aa";
                    Log.d(TAG,response.message());
                }
            }
        });
    }

    public void editUserPass(String user, String password) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newPass = new JSONObject();
        String url = "http://10.4.41.144:3000/users/password/"+user;

        try {
            newPass.put("value", password);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newPass.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body).
                        build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String TAG = "aa";
                    Log.d(TAG,response.message());
                }
            }
        });
    }

    public JSONObject getUserInfo(String email) throws JSONException {
        for(int i = 0; i < usersList.length(); ++i) {
            JSONObject us = usersList.getJSONObject(i);

            String userLogin = us.getString("EMAIL");

            if(email.equals(userLogin)) {
                return us;
            }
        }

        return null;
    }



    /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////PLACES/////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    public void updatePlacesList() {
        String url = "http://10.4.41.144:3000/establishments";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String r = response.body().string();

                    try {
                        placesList = new JSONArray(r);
                        errorMsg = "";

                        PlacesList pl = PlacesList.getInstance();
                        pl.updateList(placesList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorMsg = "connection";
                    }

                }
            }
        });
    }


    public JSONArray getPlacesListList() { return placesList;}

    public String getServerAddress() {
        return serverAddress;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public OkHttpClient getClient() {
        return client;
    }





    public void guardaReserva(Integer id, String token, Integer count, String rd, String rh) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newName = new JSONObject();
        String url = serverAddress + "/registerReservation";

        try {
            newName.put("id_establishment", id);
            newName.put("people_count", count);
            newName.put("reservation_date", rd);
            newName.put("reservation_hour", rh);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newName.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", token)
                .post(body)
                .build();
        System.out.print("hey\n");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                errorMsg = "connection";
                System.out.print("No va");
                Log.d("hey", "heyyy");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String TAG = "aa";
                    Log.d(TAG,response.message());
                    System.out.print("Deu");
                }
                else{
                    System.out.print("Hola");
                }
            }
        });
    }


    public void addValoracion(String value, String description, int idEstablishment, String measures, String token) {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject newUser = new JSONObject();
        String url = serverAddress + "/registerRating";

        try {
            newUser.put("value", value);
            newUser.put("description", description);
            newUser.put("establishment_id", idEstablishment);
            newUser.put("measures", measures);
        } catch (JSONException e) {
            Log.d("OKHTTP3", "JSON Excepton");
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(newUser.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("AUTHORIZATION", token)
                .post(body).
                        build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println("ERROR//////////////////////////////////////////7");
            e.printStackTrace();
        }
    }
}




