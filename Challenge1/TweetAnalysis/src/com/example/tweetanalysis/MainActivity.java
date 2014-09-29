package com.example.tweetanalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.tweetanalysis.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class MainActivity extends ActionBarActivity {
	
	
	Button button4;
	Button button5;
	Button button6;
	Button button7;
	WebView webView;

	private String twitterData;
	private LocationManager locationManager;
    private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/// Collecting Time Data
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 Date date = new Date();

		 /// Collecting GPS Data
		 LocationManager locationManager =
			        (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			 
			String mlocProvider;
			Criteria hdCrit = new Criteria();
			 
		
		Button button3 =(Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				System.out.println("tring to read a local file");

				FileInputStream file;
				try{
					File f = new File(Environment.getExternalStorageDirectory()+"/Data2/ADTRead.txt");
					if(f.exists()==true){
						file = new FileInputStream(f);
						System.out.println(f.getAbsolutePath());
						//System.out.println(new DataInputStream(file).readLine());
						BufferedReader bufferr = new BufferedReader(new FileReader(f));
						String line;
						String data=new String();
						 String sp = "";
						 System.out.println("hiiiiiiiii");
						 while((line = bufferr.readLine())!=null)
						 {
							 sp = line;
							 System.out.println("sp---------"+sp);
							 data=data.concat(sp+"\n");
							 System.out.println("data----"+data);
							
						 }
						// System.out.println(data);
						 //read data from the file and store 
						 twitterData=data;
						 String result=sentimentAnalysys(data);
						 
						 TextView txtview=(TextView)findViewById(R.id.textView1);
						 txtview.setText(data);
						 file.close();
						bufferr.close();
					}
					else{
						System.out.println("no file is not there");
					}		
					
				}
				catch(Exception e){
					System.out.println("exception in reading file"+e.toString());
				}
			}
 
		});
		

		Button button4 =(Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				String result=sentimentAnalysys(twitterData);
				 System.out.println("result>>>>>"+result);
				 TextView txtview=(TextView)findViewById(R.id.textView1);
				 txtview.setText(result);
				}
 
		});

	}
	
	  public void writeFile(String time)
      {
		 
		  String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/Data2");
			String fname = "GPS_Grp4.txt";
			myDir.mkdirs();
		    File file = new File (myDir, fname);
		   
		    try {
		           FileOutputStream out = new FileOutputStream(file,true);
		           out.write(time.getBytes());
		           out.write("\n".getBytes());
		           out.flush();
		           out.close();

		    } catch (Exception e) {
		           e.printStackTrace();
		    }

      }
	
	  public String sentimentAnalysys(String data)
	    {		 
		  int count1=0;
		  int count2=0;
			   		    try {
			   		    	
			   		    	System.out.println("inside sentiment analysys>>>>>"+data);
			   		   
			   		   StringTokenizer st = new StringTokenizer(data,"/");
			   		   while (st.hasMoreTokens()) {			   			   
			   			   String comp=st.nextToken();
			   			   System.out.println("tken>>>>>"+comp);
			   			   if(comp.contains("positive")){
			   				   	 count1++;
			   				   	 System.out.println("inside if"+count1);
			   			   }
			   			   else if(comp.contains("negative")){
			   				   count2++;
			   				 System.out.println("inside else>>>>>"+count2);
			   			   }			   	
			   			}
			   		   /*End of while*/
			   		  String analysys=new String("Positive Tweets : "+count1+"\t"+" Negative tweets  : "+count2);
			   		  System.out.println("analysys"+analysys);
			   		  data=analysys;
			   		/*  data=data.concat("\n");
			   		  data=data.concat("\n");
			   		  data=data.concat(analysys);*/
			   		 System.out.println("data after appending result>>>"+data);
			   		 
			    } catch (Exception e) {
			           e.printStackTrace();
			    }
						
					return data;

	    }
	 
}

class SendFile extends AsyncTask<String, Void, String> {
	

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
           
        	
        	
        	JSch ssh = new JSch();
		        JSch.setConfig("StrictHostKeyChecking", "no");
		        Session session;
				try {
					session = ssh.getSession("cloudera", "134.193.136.114", 22);
				
		        session.setPassword("your_password");
		        session.connect();
		        Channel channel = session.openChannel("sftp");
		        channel.connect();
		        ChannelSftp sftp = (ChannelSftp) channel;
		        
		        File sdCard = Environment.getExternalStorageDirectory(); 
				File directory = new File (sdCard.getAbsolutePath() + "/Data2");
			    
				Log.i(null,directory+"/GPS.txt");
				
		        
		        sftp.put(directory+"/GPS.txt", "/home/cloudera/");
		    	
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					Log.i(null,e.toString());
					e.printStackTrace();
					
				} catch (SftpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}

        	
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
		return null;
    }

    protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
  
}
