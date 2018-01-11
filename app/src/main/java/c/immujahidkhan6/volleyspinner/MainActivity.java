package c.immujahidkhan6.volleyspinner;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Declaring an Spinner
    private Spinner state_spinner, city_spinner, town_spinner, block_spinner;
    int state_id, city_id, town_id;
    String town_id_str;
    ProgressDialog progressDialog;
    //An ArrayList for Spinner Items
    private ArrayList<String> citiesList;
    private ArrayList<String> TownList;
    private ArrayList<String> blockList;
    //JSON Array
    private JSONArray cities;
    private JSONArray town;
    private JSONArray block;

    //TextViews to display details
    private TextView state, city, towntxt, blocktxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the ArrayList
        citiesList = new ArrayList<String>();
        TownList = new ArrayList<String>();
        blockList = new ArrayList<String>();

        //Initializing Spinner
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        town_spinner = (Spinner) findViewById(R.id.town_spinner);
        block_spinner = (Spinner) findViewById(R.id.block_spinner);
        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_id = position;
                if (state_id == 0) {
                    citiesList.clear();

                } else {
                    state.setText(String.valueOf(state_id));
                    Toast.makeText(MainActivity.this, String.valueOf(state_id), Toast.LENGTH_SHORT).show();
                    getCitiesData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = Integer.parseInt(getCitiesId(position));
                city.setText(getCitiesId(position));
                String checkEmpty = String.valueOf(city_id);
                if(checkEmpty=="null")
                {

                }else {
                    getTownData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        town_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //town_id = Integer.parseInt(getTownId(position));
                town_id_str = String.valueOf(getTownId(position));
                towntxt.setText(getTownId(position));
                if(town_id_str=="null")
                {

                }else {
                    getBlockData();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        block_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blocktxt.setText(getBlockId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Initializing TextViews
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        towntxt = findViewById(R.id.town);
        blocktxt = findViewById(R.id.block);

        //This method will fetch the data from the URL
        //getData();
    }

    private void getCitiesData() {
        //Creating a string request
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cities Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL + Config.fk_state_id + state_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            cities = j.getJSONArray(Config.JSON_ARRAY_cities_list);

                            //Calling method getStudents to get the students from the JSON Array
                            getCities(cities);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void getCities(JSONArray j) {
        //Traversing through all the items in the json array
        if (!citiesList.isEmpty()) {
            citiesList.clear();
        }
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                citiesList.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        city_spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, citiesList));
    }

    //Doing the same with this method as we did with getName()
    private String getCitiesId(int position) {
        String course = "";
        try {
            JSONObject json = cities.getJSONObject(position);
            course = json.getString(Config.TAG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    private void getTownData() {
        //Creating a string request
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Town Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL + Config.fk_city_id + city_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            town = j.getJSONArray(Config.JSON_ARRAY_town_list);

                            //Calling method getStudents to get the students from the JSON Array
                            getTown(town);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void getTown(JSONArray j) {
        //Traversing through all the items in the json array
        if (!TownList.isEmpty()) {
            TownList.clear();
        }
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                TownList.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        town_spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, TownList));
    }

    //Doing the same with this method as we did with getName()
    private String getTownId(int position) {
        String townId = "";
        try {
            JSONObject json = town.getJSONObject(position);
            townId = json.getString(Config.TAG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return townId;
    }

    ////block fetching
    private void getBlockData() {
        //Creating a string request
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Block Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL + Config.fk_town_id + town_id_str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            block = j.getJSONArray(Config.JSON_ARRAY_block_list);

                            //Calling method getStudents to get the students from the JSON Array
                            getBlock(block);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void getBlock(JSONArray j) {
        //Traversing through all the items in the json array
        if (!blockList.isEmpty()) {
            blockList.clear();
        }
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                blockList.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        block_spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, blockList));
    }

    //Doing the same with this method as we did with getName()
    private String getBlockId(int position) {
        String blockId = "";
        try {
            JSONObject json = block.getJSONObject(position);
            blockId = json.getString(Config.TAG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return blockId;
    }
}