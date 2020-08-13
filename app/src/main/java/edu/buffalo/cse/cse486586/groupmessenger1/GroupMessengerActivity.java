package edu.buffalo.cse.cse486586.groupmessenger1;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.ContentValues;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    String[] remote_ports = {"11108", "11112", "11116", "11120","11124"};
    //private final ContentResolver mContentResolver;
    static final int SERVER_PORT = 10000;
     int seq=0;
    //private final TextView mTextView;
    ContentResolver mContentResolver;
    private final Uri mUri;
   // private final ContentValues[] mContentValues;

    public GroupMessengerActivity() {
        //mTextView = _tv;
        //mContentResolver = _cr;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");
     //   mContentValues = initTestValues();
    }
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        mContentResolver = getContentResolver();

        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());


        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        try {
            /*
             * Create a server socket as well as a thread (AsyncTask) that listens on the server
             * port.
             *
             * AsyncTask is a simplified thread construct that Android provides. Please make sure
             * you know how it works by reading
             * http://developer.android.com/reference/android/os/AsyncTask.html
             */
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) {
            /*
             * Log is a good way to debug your code. LogCat prints out all the messages that
             * Log class writes.
             *
             * Please read http://developer.android.com/tools/debugging/debugging-projects.html
             * and http://developer.android.com/tools/debugging/debugging-log.html
             * for more information on debugging.
             */
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }

        final EditText editText = (EditText) findViewById(R.id.editText1);
        final Button btn = (Button) findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println("send button is clicked ");
                Log.v("send button is clicked","");
                String msg = editText.getText().toString() + "\n";
                editText.setText(""); // This is one way to reset the input box.
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);
                //return true;

            }
        });
       /* editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    *//*
                     * If the key is pressed (i.e., KeyEvent.ACTION_DOWN) and it is an enter key
                     * (i.e., KeyEvent.KEYCODE_ENTER), then we display the string. Then we create
                     * an AsyncTask that sends the string to the remote AVD.
                     *//*
                    System.out.println("inside ");
                    String msg = editText.getText().toString() + "\n";
                    editText.setText(""); // This is one way to reset the input box.
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);
                    return true;
                }
                return false;
            }
        });
*/
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
        
        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */
    }
    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            System.out.println("serverSocket=" + serverSocket);
            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             *
             */
            //String message;
            while(true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    //read from socket to ObjectInputStream object
                    BufferedReader ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //convert ObjectInputStream object to String
                    //message = (String) ois.readObject();
                    String message = ois.readLine();

                    //  System.out.println("Message Received: " + message);
                    //ois.close();
                    //socket.close();
                  //  if ((message = ois.readLine()) != null ) {

                        publishProgress(message);

                    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                    PrintWriter pw = new PrintWriter(osw);
                    pw.println("CLOSE");
                    Log.v(TAG,"sending the ack");
                    osw.flush();
                    socket.close();
                 /*   }
                    else{
                        break;
                    }
                 */   // to close the socket
                   } catch (Exception e) {
                   // socket.close();
                    e.printStackTrace();
                }
            }

         //  return null;
        }

        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            //System.out.println("inside onProgressUpdate");
            String strReceived = strings[0].trim();
            TextView remoteTextView = (TextView) findViewById(R.id.textView1);
            remoteTextView.append(strReceived + "\t\n");
            ContentValues values = new ContentValues();
           // ContentValues contentValues = new ContentValues();
            String seqstr = String.valueOf(seq);
            values.put("key", seqstr);
            values.put("value", strReceived);

            //gmp.onCreate();
            mContentResolver.insert(mUri,values);
            seq++;
           // gmp.getinfo();

            //  TextView localTextView = (TextView) findViewById(R.id.textView1);
           // localTextView.append("\n");

            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             *
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */

            /*String filename = "SimpleMessengerOutput";
            String string = strReceived + "\n";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "File write failed");
            }
*/
            return;
        }
    }

    /***
     * ClientTask is an AsyncTask that should send a string over the network.
     * It is created by ClientTask.executeOnExecutor() call whenever OnKeyListener.onKey() detects
     * an enter key press event.
     *
     * @author stevko
     *
     */
    private class ClientTask extends AsyncTask<String, Void, Void> {

        String msgToSend;

        @Override
        protected Void doInBackground(String... msgs) {

            String remotePort;
            Socket socket =  null;
               // System.out.println("remotePort=" + remotePort);
                System.out.println("msgs[1]=" + msgs[1]);
                for(int i=0;i<5;i++) {
                 //   if (msgs[1].equals(remote_ports[i])) {
                  //      System.out.println("Not going to send this mssg");
                  //  } else {
                        remotePort = remote_ports[i];
                        try {
                            System.out.println("remotePort=" + remotePort);

                             socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                    Integer.parseInt(remotePort));

                           //  mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");
                             msgToSend = msgs[0];
                            System.out.println("messagetoSend=" + msgToSend);
                            /*
                             * TODO: Fill in your client code that sends out a message.
                             */
                            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                            PrintWriter pw = new PrintWriter(osw);
                            pw.println(msgToSend);
                            osw.flush();

                            BufferedReader ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            //convert ObjectInputStream object to String
                            //message = (String) ois.readObject();
                            String message = ois.readLine();
                            if(message.equals("CLOSE")){
                                Log.v(TAG,"ack is received");
                                   socket.close();
                            }

                            } catch (UnknownHostException e) {
                            Log.e(TAG, "ClientTask UnknownHostException");
                        } catch (IOException e) {
                            Log.e(TAG, "ClientTask socket IOException");
                        }

                }

                return null;
            }


       }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }
}
//Final Submission