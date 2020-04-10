package com.example.shlomyslaptop.myapplication99;

// build app theoreticli till the end
// upload server to python anywher
// addons flashlight , notifcations befor landing ,faster scanning ,handle clse crash ,
//
//newtodo
//scane bus,reopen bus after cloesd,tow exe files one loadxml files to server and one from server,searchwindow,permitions for evry worker,if bus is cosed ask if restart bus,printing
// Application
// scan view with flashlight and left seats count
// add bus window that able to add/edit bus to server
// settings window to define the workers id , way , bus id, terminal
// TODO45
// add close bus function
// add option to remove passenger from bus
// handle internet error exceptions
// update seats count after avery scan
// setup terminal option in settings
// check addbus , settings  for input mistakes
// /
// Server
// stamp ticket commeand gets bus id ,ticket id ,worker id. and returns True /False with reason
// add bus command gets busid , seats , type and return True/False with reason
// update data like passengers flights buss and tickets
// TODO45
// add date to bus like worker id driver phone number and second driver and bus exit time
// fill server commands like get passengers in terminal , get next flights
// add server command to get full data in xls
// /
// Windows program
// window to load passingers and flights from xml file
// window to search a passinger flight or bus
// TODO45
// window that displays the search results for passinger flight or bus
// synchronize between the program and the server
// /


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.graphics.Color.RED;
import static me.dm7.barcodescanner.zxing.ZXingScannerView.*;

public class MainActivity extends AppCompatActivity implements ResultHandler {
    private ZXingAutofocusScannerView  zXingScannerView;
    private RequestQueue mQueue;
    public ConstraintLayout constraintLayout;
    public EditText myedit;
    public String CurrentView = "MainActivity";
    public String intres="False6";
    public String erorreson="False";
    public JSONObject passingersinbus;
    public boolean lockScreen=false;
    public String backscreen = "nune";
    public boolean waittoend = false;
    public View mview = null;
    public boolean stampanyway  = false;
    public boolean connectioneror = false;
    public boolean debogscreen = false;
    public JSONObject searchstat = new JSONObject();
    public JSONObject lastsearchresults = new JSONObject();
    public String searchonly = "all";
    public String scaneid = "None";
    public ConstraintLayout openbus;
    public String busidresults;
    public String locations;
    public String opbuid;
    public String restartsbusp = "False";
    public String worker_id = "False",cn_key = "False",last_bus = "False" , way = "False";
    public String commenttext = "no comment";
    public String openingmes = "eror ";
    public JSONObject bussans = null;
    public boolean isflash = false;
    public  PopupWindow popupconn;
    public boolean athome = false;
    public String lang = Locale.getDefault().getDisplayLanguage();
    public Typeface he_typeface;
    public String GTicketId = "00000000";
    Boolean StampByTz = false;
    RelativeLayout rl;
    ZXingScannerView zXingScannerView1;
    ////mainthings
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Toast.makeText(getApplicationContext(),"hellow ",Toast.LENGTH_LONG).show();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //Toast.makeText(getApplicationContext(),"שלום מלכי",Toast.LENGTH_LONG).show();
        he_typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.hebrow_alfa);

        super.onCreate(savedInstanceState);


        preper_app();
        mQueue = Volley.newRequestQueue(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    public void preper_app()
    {
        setpermitions();
        on_connection_eror();
        Start_login();
    }
    public void Start_login()
    {
        lockScreen = true;
        setContentView(R.layout.loginwindow);
        try{
            String login_info = readFromFile("login_info.txt",getApplicationContext());
            String user = login_info.split(",")[0],pass = login_info.split(",")[1];
            EditText us,ps;
            us = findViewById(R.id.login_name);
            ps = findViewById(R.id.login_password);
            us.setText(user);
            ps.setText(pass);
            //Toast.makeText(getApplicationContext(),login_info,Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void updatemes()
    {
        String enmes = "eror",hemes = "eror";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 5 && timeOfDay < 12){
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            enmes = "Good Morning ";
            hemes = "בוקר טוב ";
        }else if(timeOfDay >= 12 && timeOfDay < 17){
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            enmes = "Good Afternoon ";
            hemes = "צהריים טובים ";
        }else if(timeOfDay >= 17 && timeOfDay < 21){
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
            enmes = "Good Evening ";
            hemes = "ערב טוב ";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
            enmes = "Good Night ";
            hemes = "לילה טוב ";
        }else if(timeOfDay >= 0 && timeOfDay < 6){
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
            enmes = "Good Night ";
            hemes = "לילה טוב ";
        }

        if(lang.equals("עברית"))
        {
            openingmes = hemes;

        }
        else
        {
            openingmes = enmes;
        }
    }

    public void Start_home() {
        athome = true;
        //closing camera
        try {
            zXingScannerView.stopCamera();
            zXingScannerView = null;
        } catch (Exception e) {

        }
        //setting screen
        setContentView(R.layout.activity_main);
        //setting hellow text view
        TextView helotext = findViewById(R.id.hellowtext),explain_text=findViewById(R.id.textView5);
        updatemes();
        String newtext = openingmes + worker_id;
        if (lang.equals("עברית"))
        {
            explain_text.setTypeface(he_typeface);
        }
        helotext.setText(newtext);

        //setting option droplist
        final Spinner optionsdrop = findViewById(R.id.apptoolsdropdown);
        String myoptions = "open vehicles list,open vehicle,print by tz number";
        String myoptions_he = "רכבים פתוחים,פתח רכב,הדפסה לפי מספר זהות";
        if(lang.equals("עברית"))
        {
            myoptions =myoptions_he;
        }

        final List<String> spinnerArray = new ArrayList<String>(Arrays.asList(myoptions.split(",")));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsdrop.setAdapter(adapter);
        optionsdrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"clickd "+optionsdrop.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                if(optionsdrop.getSelectedItem().toString().equals(spinnerArray.get(0)))
                //meens that the user chose the first option = open vehicles
                {
                    if(!athome)
                    {
                        Start_home();
                    }



                }
                if(optionsdrop.getSelectedItem().toString().equals(spinnerArray.get(1)))
                //meens that the user chose the second option = print by tz
                {
                    athome = false;
                    OPENBUS(mview);



                }
                if(optionsdrop.getSelectedItem().toString().equals(spinnerArray.get(2)))
                //meens that the user chose the teared option = print by tz
                {
                    athome = false;
                    printqrpopup(view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //getting workers buss
        String openbussl = get_opend_buss();
        //adding listedbuss if list is not emty
        if(!openbussl.equals("empty"))
        {
            LinearLayout busslist = findViewById(R.id.vieclelist);
            busslist.removeAllViews();
            String[] blist = openbussl.split(",");
            for (final String busp : blist)
            {


                try {

                    View busv = get_view_copy(R.layout.viachle_view);
                    busv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            last_bus = busp;
                            startscane();
                        }
                    });
                    JSONObject jres = bussans.getJSONObject(busp);
                    TextView placs =  busv.findViewById(R.id.vieceleview_placec_con);
                    TextView moza = busv.findViewById(R.id.viecleview_moza_con);
                    TextView yad = busv.findViewById(R.id.vieceleview_yad_con);
                    TextView busvtext = busv.findViewById(R.id.viecleview_id_con);
                    ImageView vitype = busv.findViewById(R.id.vitype);
                    String cartype = jres.getString("car_type");
                    if(cartype.equals("minibus"))
                    {
                        vitype.setImageDrawable(getDrawable(R.drawable.tendericon));
                    }
                    if(cartype.equals("car"))
                    {
                        vitype.setImageDrawable(getDrawable(R.drawable.carricon));
                    }
                    placs.setText(jres.getString("placs"));
                    moza.setText(jres.getString("moza"));
                    yad.setText(jres.getString("yad"));
                    busvtext.setText(busp);
                    busslist.addView(busv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }

    }





    ///qrpopup
    public void printqrpopup(View view)
    {
        final View pop = get_view_copy(R.layout.printticketview);
        LinearLayout busslist = findViewById(R.id.vieclelist);
        busslist.removeAllViews();
        busslist.addView(pop);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //PopupWindow popupWindow = new PopupWindow(pop, width, height, true);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        //popupWindow.showAtLocation(view, Gravity.TOP, 0, 200);
        final ImageButton printbtn = pop.findViewById(R.id.printqr);
        printbtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText pass_tz = pop.findViewById(R.id.misparzehutinput);
                String tz = pass_tz.getText().toString();
                if (!tz.equals("")) {
                    try {
                        JSONObject qrreq = new JSONObject();
                        qrreq.put("act", "get_passinger_qr");
                        qrreq.put("tz", tz);
                        String sres = URLconnection(getString(R.string.serverurl), qrreq.toString());
                        JSONObject res = new JSONObject(sres);
                        if (res.getString("ans").equals("true")) {
                            String qrdata = res.getString("qrdata");
                            //print it
                            Bitmap mBitmap = StringToBitMap(qrdata);
                            printbitmap(mBitmap);

                        } else {
                            Toast.makeText(getApplicationContext(), res.getString("reson"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "משהו השתבש..", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void printbitmap(Bitmap bitmap)
    {
        PrintHelper mPrintHelper = new PrintHelper(this);
        mPrintHelper.setColorMode(PrintHelper.SCALE_MODE_FIT);
        //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/reseat.png");
        mPrintHelper.printBitmap("print qr for ", bitmap);
    }
    ///all scane stuff
    public void refreshhome(View view)
    {
        Start_home();
    }
    public void startscane()
    {
        //setView
        setContentView(R.layout.costemscane);
        //set up the camera
        zXingScannerView = new ZXingAutofocusScannerView (this);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_scan_take_single1);
        rl.addView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.setBorderColor(getColor(R.color.colorAccent));
        List<BarcodeFormat> myformat = new ArrayList<>();
        myformat.add(BarcodeFormat.QR_CODE);
        zXingScannerView.setFormats(myformat);
        zXingScannerView.setFlash(isflash);
        zXingScannerView.startCamera();
        //update bus date
        update_scane_view();
    }
    public void update_scane_view()
    {
        //update bus data
        try
        {
            stampanyway = false;
            final String busid = getbus();
            if (busid.equals("nobus"))
            {
                Start_home();
                Toast.makeText(getApplicationContext(),getResources().getText(R.string.faildnobustext),Toast.LENGTH_LONG).show();
            }
            else
            {
                //setting stamp anywaydefult to false
                stampanyway = false;
                //getting bus info
                JSONObject req = new JSONObject();
                req.put("act", "startscane");
                req.put("busid", busid);

                String businfo = "no bus";
                businfo = URLconnection(getString(R.string.serverurl), req.toString());
                JSONObject ans = new JSONObject(businfo);
                if (ans.getString("ans").equals("True")) {
                    JSONObject jbusinfo = ans.getJSONObject("bus");
                    TextView seatsbtn = findViewById(R.id.seatscounttext);
                    TextView busid_btn = findViewById(R.id.scane_businfo);
                    TextView workername_text = findViewById(R.id.worker_name);
                    workername_text.setText(worker_id);
                    seatsbtn.setText(jbusinfo.getString("seatsleft"));
                    busid_btn.setText(busid);
                    JSONObject passengers_list = ans.getJSONObject("passengers_list");
                    for (Iterator<String> it = passengers_list.keys(); it.hasNext(); ) {
                        String passenger_key = it.next();
                    }



                }
                else
                {
                    if (ans.getString("reson").equals("no bus"))
                    {
                        Start_home();
                        Toast.makeText(getApplicationContext(),"אין כזה רכב"+busid,Toast.LENGTH_LONG).show();
                    }
                    if (ans.getString("reson").equals("bus_is_closed")) {
                        //Toast.makeText(getApplicationContext(), "bus is close", Toast.LENGTH_LONG).show();
                        final Runnable onyes, onno;
                        onyes = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    restartsbusp = "True";
                                    opbuid = busid;
                                    OPENBUS(mview);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "התהליך נכשל", Toast.LENGTH_LONG).show();
                                }

                            }
                        };
                        onno = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject req = new JSONObject();
                                    req.put("act", "reopenbus");
                                    req.put("busid", busid);
                                    String respond = URLconnection(getString(R.string.serverurl), req.toString());
                                    JSONObject jres = new JSONObject(respond);
                                    if (jres.getString("ans").equals("True")) {
                                        Toast.makeText(getApplicationContext(), "האוטובוס נטען בהצלחה", Toast.LENGTH_LONG).show();
                                    } else {
                                        Start_home();
                                        Toast.makeText(getApplicationContext(), "השרת קרס" + jres.toString(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "התהליך נכשל", Toast.LENGTH_LONG).show();
                                }
                            }
                        };
                        ask("מה תרצה לעשות?", "התחל מחדש", "טען נסיעה קודמת", onyes, onno);
                        return;
                    }
                    if (ans.getString("reson").equals("bus_is_not_opend"))
                    {
                        Toast.makeText(getApplicationContext(),"האוטובוס לא פתוח",Toast.LENGTH_LONG).show();
                        opbuid = busid;
                        OPENBUS(mview);

                    }
                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"משהו השתבש..",Toast.LENGTH_LONG).show();

        }

    }
    @Override
    public void handleResult(Result rawResult)
    {

        try {
            on_scane_results(rawResult.getText());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString()+"somthin went rung",Toast.LENGTH_SHORT).show();
        }

        zXingScannerView.resumeCameraPreview(this);

    }
    public void on_scane_results(String ticket_id) throws JSONException {
        GTicketId = ticket_id;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("act","stamp_ticket");
        jsonObject.put("bus_id",last_bus);
        jsonObject.put("ticket_id",ticket_id);
        jsonObject.put("worker_id",worker_id);
        jsonObject.put("bytz",StampByTz);
        StampByTz = false;


        if(stampanyway){
            jsonObject.put("stampanyway",true);
            stampanyway = false;
        }
        String res=null;
        String url = getString(R.string.serverurl);
        try {
            res = URLconnection(url,jsonObject.toString());
            on_stamp_results(res);

        }catch (Exception e)
        {
            Log.println(1,"GD","no idie");
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"no internet"+res,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }





    }

    public void HomeBtn(View view)
    {
        Start_home();
    }
    public void stampanyway_btn(View view) throws JSONException {
        stampanyway = true;
        on_scane_results(GTicketId);

    }

    public void on_stamp_results(String res) {

        try {
            update_scane_view();

            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
            final JSONObject jresult = new JSONObject(res);
            String pname = "No name",reson = "no reson";




            reson  = jresult.optString("reson",reson);
            if (jresult.getString("ans").equals("True"))
            {
                pname = jresult.optString("pname_en",pname);
                if(lang.equals("עברית"))
                {
                    pname = jresult.getString("pname_he");
                }
                popup_scaneresults(true,pname,"good",false);
                Toast.makeText(getApplicationContext(), getString(R.string.goodtext),Toast.LENGTH_LONG).show();
                vibrait(700);

            }
            else{
                if(jresult.getString("reson").equals("ticket_is_uesd_in_this_bus"))
                {
                    waittoend = true;
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        unstamp_ticket(jresult.getString("ticket"));
                                        update_scane_view();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked

                                    break;


                            }


                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage(getResources().getString(R.string.messageticketinbus)).setPositiveButton(getResources().getString(R.string.yestext), dialogClickListener).setNegativeButton(getResources().getString(R.string.notext), dialogClickListener).show();
                }
                else if (jresult.getString("reson").equals("eror:no_ticket"))
                {
                    pname = jresult.getString("ticket_id");
                    reson = getString(R.string.eror_text_no_ticket);
                    popup_scaneresults(false,pname,reson,true);
                }
                else if (jresult.getString("reson").equals("eror:ticket_used"))
                {
                    pname = jresult.getString("pname_en");
                    if(lang.equals("עברית"))
                    {
                        pname = jresult.getString("pname_he");
                    }
                    reson = getString(R.string.eror_text_ticket_used);
                    popup_scaneresults(false,pname,reson,true);
                }
                else if (jresult.getString("reson").equals("eror:ticket_car_type_no_fit"))
                {
                    reson = jresult.getString("reson");
                    String ve_type = jresult.getString("bus_cartype");
                    String tic_type = jresult.getString("ticket_cartype");
                    pname = jresult.getString("pname_en");
                    if(lang.equals("עברית"))
                    {
                        pname = jresult.getString("pname_he");
                        tic_type.replace("bus","אוטובוס");
                        tic_type.replace("minibus","מיניבוס");
                        ve_type.replace("bus","אוטובוס");
                        ve_type.replace("minibus","מיניבוס");

                    }
                    reson = getString(R.string.eror_text_car_dont_fit);
                    reson = reson.replace("change1",tic_type);
                    popup_scaneresults(false,pname,reson,true);
                }
                //fill moza and yad dont fit 
                else if (jresult.getString("reson").equals("eror:moza_dont_fit"))
                {
                    pname = jresult.getString("pname_en");
                    if(lang.equals("עברית"))
                    {
                        pname = jresult.getString("pname_he");
                    }
                    String busmoza = jresult.getString("bus_moza");
                    reson = getString(R.string.mozadontfit);
                    reson = reson.replace("change1",busmoza);
                    popup_scaneresults(false,pname,reson,true);
                }
                else if (jresult.getString("reson").equals("eror:yad_dont_fit"))
                {
                    pname = jresult.getString("pname_en");
                    if(lang.equals("עברית"))
                    {
                        pname = jresult.getString("pname_he");
                    }
                    String busmoza = jresult.getString("bus_yad");
                    reson = getString(R.string.mozadontfit);
                    reson = reson.replace("change1",busmoza);
                    popup_scaneresults(false,pname,reson,true);
                }

                else {
                    popup_scaneresults(false,pname,reson,true);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "כירטוס נכשל \n"+res,Toast.LENGTH_LONG).show();
        }
    }
    public void popup_scaneresults(Boolean ans,String passname,String reson,boolean btn)
    {

        View scanresv = get_view_copy(R.layout.scaneresultview);
        View backview = scanresv.findViewById(R.id.backview9);
        TextView passnameT = scanresv.findViewById(R.id.passname);
        TextView resonT = scanresv.findViewById(R.id.anscomment);
        passnameT.setText(passname);
        if(ans)
        {
            play_sound("good");
        }
        else
            {
                play_sound("bad");

                backview.setBackgroundDrawable(getDrawable(R.drawable.stampbadb));
                if(btn)
                {
                    scanresv.findViewById(R.id.stamp_anyway_btn).setVisibility(VISIBLE);
                }
                resonT.setText(reson);
            }
        //poping up view
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(scanresv, width, height, focusable);
        popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
        scanresv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        //a
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                try{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                        }
                    });

                }
                catch (Exception e){

                }


            }
        }).start();
    }
    public View get_view_copy(int viewid)
    {
        final View copyedview;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        copyedview = inflater.inflate(viewid, null, true);
        return copyedview;
    }
    public String get_opend_buss()
    {
        String ans = "sumthing is rong,bus2,bus3,bus4";
        try
        {
            JSONObject reqwestob = new JSONObject();
            reqwestob.put("act","get_my_opend_buss");
            reqwestob.put("worker_id",worker_id);
            String res = URLconnection(getString(R.string.serverurl),reqwestob.toString());
            JSONObject jres = new JSONObject(res);
            bussans = jres;
            ans = jres.getString("allkeys");
        }
        catch (Exception e)
        {

        }
        return ans;

    }
    @Override
    public void onBackPressed()
    {
        if(!backscreen.equals("nune"))
        {

            if(backscreen.equals("openbus"))
            {
                backscreen = "nune";
                setContentView(R.layout.activity_main);
                OPENBUS(mview);

            }
            return;
        }
        if(lockScreen==false)
        {
            Start_home();
        }



    }
    public void loginH(View view)
    {
        EditText name = findViewById(R.id.login_name);
        EditText password = findViewById(R.id.login_password);
        Button login_btn = findViewById(R.id.login_login);
        JSONObject req = new JSONObject();
        try{
            req.put("act","login");
            req.put("worker_id",name.getText().toString());
            req.put("password",password.getText().toString());
            String res = URLconnection(getString(R.string.serverurl),req.toString());
            JSONObject jres = new JSONObject(res);
            if(jres.get("ans").equals("True"))
            {
                writeToFile(req.getString("worker_id")+","+req.getString("password"),"login_info.txt",getApplicationContext());
                worker_id = req.getString("worker_id");
                cn_key = jres.getString("cnkey");
                lockScreen = false;
                Start_home();
            }else {
                Toast.makeText(getApplicationContext(),"שם וסיסמא לא תואמים",Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"התהליך נכשל",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public boolean on_connection_eror() {
        boolean hase_eror = false;
        String eror_reson = "no reson";
        hase_eror = false;
        if (isInternetAvailable()==false)
        {
            hase_eror = true;
            eror_reson = "no internet";
        }
        if(hase_eror==false)
        {
            if (!checkconnection())
            {
                hase_eror = true;
                eror_reson = "no server";
            }
        }
        if(hase_eror==true)
        {
            setContentView(R.layout.connection_erors);
        }
        Thread metred = new Thread(new Runnable() {
            String erormes = "checking connection";
            final TextView mes = findViewById(R.id.eror_mes);
            final Handler handler = new Handler();
            ConstraintLayout mlay = (ConstraintLayout) findViewById(R.id.connection_eror_lay);

            public void run(){
                lockScreen = true;
                boolean hav_eror = true;
                while (hav_eror){
                    if(!isInternetAvailable()){
                        erormes = "you have no internet";
                    }else {
                        hav_eror = false;
                        if(!checkconnection()){
                            erormes = "ther is problem with server";
                            hav_eror =true;
                        }else {
                            hav_eror = false;
                        }
                    }
                    if(hav_eror){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mes.setText(erormes);
                                mlay.setBackgroundColor(RED);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){

                        }

                    }


                }

            }
        });
        if(hase_eror==true){
            metred.start();
        }
        return hase_eror;






    }
    ////scane stuff
    public void addanonimospassenger(final View view)
    {
        Runnable onyes = new Runnable() {
            @Override
            public void run() {
                JSONObject req = new JSONObject();
                try
                {
                    req.put("act","add_anonimus_to_bus");
                    req.put("bus_id",getbus());
                    String ans = URLconnection(getString(R.string.serverurl),req.toString());
                    Toast.makeText(getApplicationContext(),"susess",Toast.LENGTH_SHORT).show();
                    update_scane_view();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"eror",Toast.LENGTH_SHORT).show();

                }
                //reqwest from server to add anonimos passenger
            }
        };
        Runnable onno = new Runnable() {
            @Override
            public void run() {

                //reqwest from server to add passenger by id
                Toast.makeText(getApplicationContext(), "add hear", Toast.LENGTH_LONG).show();
                final View getTzView = get_view_copy(R.layout.add_by_id_reciver);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(getTzView, width, height, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                final ImageButton nextbtn = getTzView.findViewById(R.id.nextbtn);
                if (lang.equals("עברית"))
                {
                    nextbtn.setImageDrawable(getDrawable(R.drawable.next_icon_h));
                }
                nextbtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText tzReciver = getTzView.findViewById(R.id.tzreciver);
                        JSONObject req = new JSONObject();

                        try {
                            req.put("act","get_ticket_id_from_tz");
                            req.put("tz",tzReciver.getText().toString());
                            String ans = URLconnection(getString(R.string.serverurl),req.toString());
                            JSONObject ansj = new JSONObject(ans);
                            if(ansj.get("ans").equals("true"))
                            {
                                on_scane_results(ansj.getString("ticket_id"));

                            }
                            else
                            {
                                popup_scaneresults(false,"no persion","ticket duse not exsist",false);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"stamping by tz went wrong",Toast.LENGTH_LONG).show();
                        }
                        popupWindow.dismiss();
                    }
                });
            }
        };
        ask(getString(R.string.addanonimosq),getString(R.string.by_anonimusli),getString(R.string.by_id_number),onyes,onno);
    }
    public void ask(String qweshton, String ans1, String ans2, final Runnable onans1,final Runnable onans2)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        onans1.run();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        onans2.run();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(qweshton).setPositiveButton(ans1, dialogClickListener)
                .setNegativeButton(ans2, dialogClickListener).show();
    }
    public void  startscane2(View view)
    {
        scane22(view);
    }
    public void scane22(View view)
    {
        zXingScannerView1 = new ZXingAutofocusScannerView(this);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        //setContentView(zXingScannerView1);
        final PopupWindow popupWindow = new PopupWindow(zXingScannerView1,width,1000,true);
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        ResultHandler custemhandle = new ResultHandler() {
            @Override
            public void handleResult(Result rawResult) {
                Toast.makeText(getApplicationContext(),rawResult.getText(),Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                opbuid = rawResult.getText();
                EditText bus_id_reciver = findViewById(R.id.openbus_busid);
                bus_id_reciver.setText(opbuid);

            }
        };
        zXingScannerView1.setResultHandler(custemhandle);
        zXingScannerView1.setBorderColor(getColor(R.color.colorAccent));
        List<BarcodeFormat> myformat = new ArrayList<>();
        myformat.add(BarcodeFormat.QR_CODE);
        zXingScannerView1.setFormats(myformat);
        zXingScannerView1.setAutoFocus(true);
        zXingScannerView1.setFlash(isflash);
        zXingScannerView1.startCamera();




    }
    public void closebusdiolog(final View view)
    {
        View popupView = get_view_copy(R.layout.closebuspopup);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        final EditText coment = popupView.findViewById(R.id.commenttext);
        Button closebusbtn = popupView.findViewById(R.id.closebusbtn1);
        closebusbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zXingScannerView.stopCamera();
                zXingScannerView = null;
                popupWindow.dismiss();
                commenttext = coment.getText().toString();
                Closebus(view);
            }
        });

    }
    public void Closebus( View view)
    {

        try {

            String reseat_text = "blh blh ";
            String workern = worker_id;
            JSONObject req = new JSONObject();
            req.put("act","closebus");
            req.put("busid",getbus());
            req.put("worker_id",workern);
            req.put("worker_comment",commenttext);
            String res =URLconnection(getString(R.string.serverurl),req.toString());
            Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
            if(res.equals("Faild"))
            {
                on_connection_eror();
                return;
            }

            JSONObject jres = new JSONObject(res);
            if(jres.getString("ans").equals("True"))
            {
                reseat_text = jres.getString("reseat");
                Toast.makeText(getApplicationContext(),reseat_text,Toast.LENGTH_LONG).show();
                //add printing details
                PrintHelper mPrintHelper = new PrintHelper(this);
                mPrintHelper.setColorMode(PrintHelper.SCALE_MODE_FIT);
                Bitmap mBitmap = StringToBitMap(jres.getString("reset-bitmap"));
                //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/reseat.png");
                mPrintHelper.printBitmap("test print", mBitmap);

                Start_home();
                Toast.makeText(getApplicationContext(),"הצלחה!!",Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getApplicationContext(),"משהו השתבש",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"נכשל משהו השתבש",Toast.LENGTH_SHORT);
        }



    }
    public Bitmap StringToBitMap(String encodedString)
    {

        Bitmap img = null;
        try {
            JSONObject json;
            json = new JSONObject(encodedString);
            String mat_string = json.getString("img");
            byte[] raw_data = Base64.decode(mat_string, Base64.DEFAULT);
            img = BitmapFactory.decodeByteArray(raw_data, 0, raw_data.length);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"נכשל בבנית תמונה",Toast.LENGTH_LONG).show();
        }
        return img;

    }



    public void FlashlightT(View view)
    {
        ImageButton btn = findViewById(R.id.Flashbtn);
        if (zXingScannerView.getFlash())
        {
            zXingScannerView.setFlash(false);
            isflash = false;
            btn.setImageDrawable(getDrawable(R.drawable.b_flash));
        }
        else
        {
            zXingScannerView.setFlash(true);
            isflash = true;
            btn.setImageDrawable(getDrawable(R.drawable.b_flash_on));

        }


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        try{
            zXingScannerView.stopCamera();
            zXingScannerView = null;
            Start_home();
        }
        catch (Exception e)
        {

        }

    }
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            zXingScannerView.stopCamera();
            zXingScannerView = null;

        }
        catch (Exception e)
        {

        }


    }

    public void unstamp_ticket(String ticket)
    {
        try {
            JSONObject req = new JSONObject();
            req.put("act","unstamp_ticket");
            req.put("ticket_id",ticket);
            req.put("bus_id",getbus());
            String res =URLconnection(getString(R.string.serverurl),req.toString());
            Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
            if(res.equals("Faild")){
                on_connection_eror();
            }else{
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"unstamping crashed\n"+e.toString(),Toast.LENGTH_SHORT).show();
        }



    }

    ////editingbusstuff
    public String getbus()
    {
        return last_bus;
    }
    public void OPENBUS(View view)
    {
        //done
        //this definition shows the openbus view in the playground aria
        //getting the open bus view
        View openbusinlayV = get_view_copy(R.layout.openbuswin);
        //getting the playground view
        LinearLayout busslist = findViewById(R.id.vieclelist);
        //removing all stuff from playground aria
        busslist.removeAllViews();
        //adding the openbus view to the playground
        busslist.addView(openbusinlayV);
        //setting bus id if scaned
        EditText iebusid = findViewById(R.id.openbus_busid);
        final EditText ebus_id = findViewById(R.id.openbus_busid);
        if(opbuid != null)
        {
            iebusid.setText(opbuid);
        }
        //filling locations in spinniers
        final Spinner SMoza = findViewById(R.id.openbus_moza);
        final Spinner SYad = findViewById(R.id.openbus_yad);

        if(locations==null)
        {
            update_locations();
        }

        List<String> spinnerArray = new ArrayList<String>(Arrays.asList(locations.split(",")));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SMoza.setAdapter(adapter);
        SYad.setAdapter(adapter);
        //seting onclick lisiner for saveandscane btn
        Button saveandscan = findViewById(R.id.openbus_saveandscane);

        saveandscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //getting bus details
                String MozaT = SMoza.getSelectedItem().toString();
                String YadT = SYad.getSelectedItem().toString();
                String busidT = ebus_id.getText().toString();
                //setting auto haloc/hazor
                String way = "haloc";
                if (MozaT.equals("uman"))
                {
                    way = "hazor";
                }
                //creating request
                JSONObject jsonObject1 = new JSONObject();
                try
                {
                    //filling bus details
                    jsonObject1.put("act","openbus");
                    jsonObject1.put("busid",busidT);
                    jsonObject1.put("moza",MozaT);
                    jsonObject1.put("yad",YadT);
                    jsonObject1.put("way",way);
                    jsonObject1.put("restart",restartsbusp);
                    jsonObject1.put("worker_id",worker_id);
                    //posting request to server
                    String res = URLconnection(getString(R.string.serverurl),jsonObject1.toString());
                    //handling results
                    JSONObject jres = new JSONObject(res);
                    if (jres.get("ans").equals("True"))
                    {
                        //means bus was opened successfully

                        //setting current bus
                        last_bus = busidT;
                        //starting scan
                        startscane();


                    }
                    else
                    {
                        //means something went rung

                        //handling error
                        String fail_reson = jres.optString("reson");
                        if (fail_reson.equals(null))
                        {
                            fail_reson = "defult eror";
                        }
                        if (fail_reson.equals("no bus"))
                        {
                            fail_reson = getString(R.string.no_car_text);
                        }
                        //displaying fail reason
                        popup_scaneresults(false,getString(R.string.Faild_text),fail_reson,false);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
    }
    ////tools
    public void play_sound(String name)
    {
        Toast.makeText(getApplicationContext(),"plaing sounf",Toast.LENGTH_SHORT).show();
        if(name.equals("good"))
        {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.succses);
            mp.start();

        }
        if(name.equals("bad"))
        {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.faild);
            mp.start();
        }

    }
    public void update_locations()
    {
        //done
        //creating request to get locations
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act","get_locations");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //posting request
        try {
            locations  = URLconnection(getString(R.string.serverurl),jsonObject.toString());
            if(locations.equals("Faild")){
                on_connection_eror();
                return;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            on_connection_eror();
            return;
        }
    }
    private String readFromFile(String filefp,Context context) {
        filefp = filefp;
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filefp);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String data,String filefp,Context context) {
        filefp = filefp;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filefp, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            e.printStackTrace();
        }
    }
    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }
    public boolean checkconnection() {
        boolean connectio = false;
        try {
            JSONObject req = new JSONObject();
            req.put("act","checkconnection");
            String res =URLconnection(getString(R.string.serverurl),req.toString());
            if(res.equals("True")){
                connectio = true;
            }

        }catch (Exception e){

        }
        return connectio;
    }
    public String  performPostCall(String requestURL, String postString)
    {

        URL url;
        String response = "";
        erorreson = "False";
        //ezTost(postString);

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(90000);
            conn.setConnectTimeout(90000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
            writer.write(postString);
            writer.flush();
            writer.close();

            os.close();
            int responseCode=conn.getResponseCode();
            try {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            catch (Exception  e) {
                erorreson = e.toString();
                e.printStackTrace();
            }

        } catch (Exception e) {
            erorreson = e.toString();
            e.printStackTrace();

        }
        //Toast.makeText(getApplicationContext(), "response is \n"+response,Toast.LENGTH_SHORT).show();
        if (response.equals(""))
        {
            response = "Faild";
        }
        intres = response;
        return intres;
    }
    class GETurlThred extends Thread
    {
        long minPrime;
        private Context context;
        private String data;
        private String url;

        GETurlThred(long minPrime,String url,String data) {
            this.minPrime = minPrime;
            this.data = data;
            this.url =  url;

        }



        public void run() {

            try {
                //View conview = get_view_copy(R.id.connectinview);
                //popupconn = new PopupWindow(conview, 0, 0, false);
                //popupconn.showAtLocation(mview, Gravity.CENTER, 0, 0);
                performPostCall(this.url,this.data);





            }
            catch (Exception e)
            {
                e.printStackTrace();
                intres = "Faild";
                erorreson = e.toString();
            }



        }
    }
    public String URLconnection(String url,String postdata) throws InterruptedException
    {
        try {
            JSONObject jp = new JSONObject(postdata);
            jp.put("workername",worker_id);
            jp.put("cnkey",cn_key);
            postdata = jp.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        GETurlThred p = new GETurlThred(143,url,postdata);
        intres = "False6";
        p.start();
        long start = System.currentTimeMillis();
        long end = start + 90000;
        while (intres.equals("False6"))
        {
            Thread.sleep(200);
            if(System.currentTimeMillis() > end) {
                return "Faild";
            }
        }
        return intres;
    }
    public void vibrait(int time)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(time);
        }
    }
    public void setpermitions()
    {

       checkpermitions();
    }
    public boolean checkpermitions()
    {
        if(!Permissons.Check_STORAGE(MainActivity.this))
        {
            //if not permisson granted so request permisson with request code
            goToSettings();
        }
        if(!Permissons.Check_CAMERA(MainActivity.this))
        {
            //if not permisson granted so request permisson with request code
            goToSettings();
        }
        return true;
    }
    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }




}
//settings.txt > worker_id/#/haloc/hazor
//buss.txt > last_buss/#/bus_befor/#/bus_befor
//
//
// /
