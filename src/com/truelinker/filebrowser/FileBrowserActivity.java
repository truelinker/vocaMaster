package com.truelinker.filebrowser;

import java.io.BufferedReader;
import java.io.File; 
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException; 
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Collections; 
import java.util.List; 
import java.util.StringTokenizer;

//import org.apache.harmony.luni.util.Msg;
import com.truelinker.R;
import com.truelinker.voca_mem.Drawer;
import com.truelinker.voca_mem.FileList;
import com.truelinker.voca_mem.Voca_Mem;
import com.truelinker.service.Voca_ManageDB;
//import com.mg.android.service.VocaProvider;

import android.app.AlertDialog; 
import android.app.ListActivity; 
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface; 
import android.content.Intent; 
import android.content.DialogInterface.OnClickListener; 
import android.graphics.drawable.Drawable; 
import android.net.Uri; 
import android.os.Bundle; 
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View; 
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView; 
import android.widget.Toast;


public class FileBrowserActivity extends ListActivity implements Runnable {
    private enum DISPLAYMODE{ ABSOLUTE, RELATIVE; } 

    private final DISPLAYMODE displayMode = DISPLAYMODE.RELATIVE; 
    private ArrayList<IconifiedText> directoryEntries = new ArrayList<IconifiedText>(); 
    private File currentDirectory = new File("/");
    
    AlertDialog alertDialog = null;
    private ProgressDialog pd;
    File clickedFile = null;
    
    public static final int MSG_FINISH_THREAD = 1;
    public static final int  MSG_MOVE_TO_VOCA = 2;
    public static final int  MSG_WRONG_FILE_FORMAT = 3;

    /** Called when the activity is first created. */ 
    @Override 
    public void onCreate(Bundle icicle) { 
         super.onCreate(icicle); 
         browseToRoot(); 
    } 
     
    /** 
     * This function browses to the 
     * root-directory of the file-system. 
     */ 
    private void browseToRoot() { 
         browseTo(new File("/sdcard")); 
   } 
     
    /** 
     * This function browses up one level 
     * according to the field: currentDirectory 
     */ 
    private void upOneLevel(){ 
         if(this.currentDirectory.getParent() != null) 
              this.browseTo(this.currentDirectory.getParentFile()); 
    } 
     
    private void browseTo(final File aDirectory){ 
    	
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
   
     //리스트 뷰 추가 
     LinearLayout listlayout = new LinearLayout(this);
     listlayout.setOrientation(LinearLayout.VERTICAL);
     listlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,   LayoutParams.WRAP_CONTENT));
     
         // On relative we display the full path in the title. 
         if(this.displayMode == DISPLAYMODE.RELATIVE) 
              this.setTitle(aDirectory.getAbsolutePath() + " :: " ); 
                        //getString(R.string.app_name)); 
         if (aDirectory.isDirectory()){ 
              this.currentDirectory = aDirectory; 
              fill(aDirectory.listFiles()); 
         }else{ 
              OnClickListener okButtonListener = new OnClickListener(){ 
                   // @Override 
                   public void onClick(DialogInterface arg0, int arg1) { 
                             // Lets start an intent to View the file, that was clicked... 
                	   		FileBrowserActivity.this.openFile(aDirectory); 
                   } 
              }; 
              OnClickListener cancelButtonListener = new OnClickListener(){ 
                   // @Override 
                   public void onClick(DialogInterface arg0, int arg1) { 
                        // Do nothing ^^ 
                   } 
              }; 
              alertDialog = new AlertDialog.Builder(this).create();
              
              alertDialog.setTitle("Question");
              alertDialog.setMessage("Do you want to open that file?\n" + aDirectory.getName());
              alertDialog.setIcon(R.drawable.folder);
              alertDialog.setButton("OK",new DialogInterface.OnClickListener(){
            	  public void onClick(DialogInterface arg0, int arg1) { 
                      // Lets start an intent to View the file, that was clicked... 
         	   		FileBrowserActivity.this.openFile(aDirectory); 
            } });
              alertDialog.setButton("Cancel",new DialogInterface.OnClickListener(){
            	  public void onClick(DialogInterface arg0, int arg1) { 
                      // Do nothing ^^ 
                 } 
              });  

         } 
    } 
     
    private void openFile(File aFile) { 
         Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, 
                   Uri.parse("file://" + aFile.getAbsolutePath())); 
         startActivity(myIntent); 
    } 

    private void fill(File[] files) { 
    		if(files == null)
    			return;
         this.directoryEntries.clear(); 
          
         // Add the "." == "current directory" 
         this.directoryEntries.add(new IconifiedText( 
                   getString(R.string.current_dir), 
                   getResources().getDrawable(R.drawable.folder)));        
         // and the ".." == 'Up one level' 
         if(this.currentDirectory.getParent() != null) 
              this.directoryEntries.add(new IconifiedText( 
                        getString(R.string.up_one_level), 
                        getResources().getDrawable(R.drawable.uponelevel))); 
          
         Drawable currentIcon = null; 
         for (File currentFile : files){ 
              if (currentFile.isDirectory()) { 
                   currentIcon = getResources().getDrawable(R.drawable.folder); 
              }else{ 
                   String fileName = currentFile.getName(); 
                   /* Determine the Icon to be used, 
                    * depending on the FileEndings defined in: 
                    * res/values/fileendings.xml. */ 
                   if(checkEndsWithInStringArray(fileName, getResources(). 
                                       getStringArray(R.array.fileEndingImage))){ 
                        currentIcon = getResources().getDrawable(R.drawable.image); 
                   }else if(checkEndsWithInStringArray(fileName, getResources(). 
                                       getStringArray(R.array.fileEndingWebText))){ 
                        currentIcon = getResources().getDrawable(R.drawable.webtext); 
                   }else if(checkEndsWithInStringArray(fileName, getResources(). 
                                       getStringArray(R.array.fileEndingPackage))){ 
                        currentIcon = getResources().getDrawable(R.drawable.packed); 
                   }else if(checkEndsWithInStringArray(fileName, getResources(). 
                                       getStringArray(R.array.fileEndingAudio))){ 
                        currentIcon = getResources().getDrawable(R.drawable.audio); 
                   }else{ 
                        currentIcon = getResources().getDrawable(R.drawable.text); 
                   }                    
              } 
              switch (this.displayMode) { 
                   case ABSOLUTE: 
                        /* On absolute Mode, we show the full path */ 
                        this.directoryEntries.add(new IconifiedText(currentFile 
                                  .getPath(), currentIcon)); 
                        break; 
                   case RELATIVE: 
                        /* On relative Mode, we have to cut the 
                         * current-path at the beginning */ 
                        int currentPathStringLenght = this.currentDirectory. 
                                                                getAbsolutePath().length(); 
                        this.directoryEntries.add(new IconifiedText( 
                                  currentFile.getAbsolutePath(). 
                                  substring(currentPathStringLenght), 
                                  currentIcon)); 

                        break; 
              } 
         } 
         Collections.sort(this.directoryEntries); 
          
         IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this); 
         itla.setListItems(this.directoryEntries);          
         this.setListAdapter(itla); 
    } 

    @Override 
    protected void onListItemClick(ListView l, View v, int position, long id) { 
         super.onListItemClick(l, v, position, id); 

         
         
         String selectedFileString = this.directoryEntries.get(position) 
                   .getText(); 
         if (selectedFileString.equals(getString(R.string.current_dir))) { 
              // Refresh 
              this.browseTo(this.currentDirectory); 
         } else if (selectedFileString.equals(getString(R.string.up_one_level))) { 
              this.upOneLevel(); 
         } else { 
             // File clickedFile = null; 
              switch (this.displayMode) { 
                   case RELATIVE: 
                        clickedFile = new File(this.currentDirectory 
                                  .getAbsolutePath() 
                                  + this.directoryEntries.get(position) 
                                            .getText()); 
                        break; 
                   case ABSOLUTE: 
                        clickedFile = new File(this.directoryEntries.get( 
                                  position).getText()); 
                        break; 
              }
              // DB update
              pd = ProgressDialog.show(this, "Working..", "Putting words into the Database", true,
      				false);

      		Thread thread = new Thread(this);
      		thread.start();
              /*
             Intent i = new Intent(this,com.mg.android.service.DBService.class);
             i.putExtra("EXTRA_NAME", "EXTRA_VALUE");
             Uri uri = Uri.parse("file:/" + clickedFile.getPath());
             i.setData(uri); 
             startService(i);
             */
              if (clickedFile != null) 
                   this.browseTo(clickedFile); 
         } 
    } 
    
	public void run() {
		String mFileName = clickedFile.getPath();
		String mOnlyFileName = null;
		//ContentResolver cr = getContentResolver();
		//ContentValues values = new ContentValues(); 
		int index = 0;
		index = mFileName.lastIndexOf(".csv");
		Log.e("TEST_LMG","index = " + index);
		if(index <= 0)
		{
			//browseTo()
			handler.sendEmptyMessage(MSG_FINISH_THREAD);

			index = mFileName.lastIndexOf(".xls");
			Log.e("TEST_LMG","index = " + index);
			if(index <= 0)
			{
				//browseTo()
				handler.sendEmptyMessage(MSG_FINISH_THREAD);

				return;
			}		
		}

		mOnlyFileName = mFileName.replaceFirst("/sdcard/", "");
		int result = Voca_ManageDB.checkFile(mOnlyFileName);
		if(result < 0)
		{
			handler.sendEmptyMessage(MSG_MOVE_TO_VOCA);
			return;
		}
		//File fullpathfile;
		//mFileName = mFileName.replaceAll("/","");
		
		try {
			// check if species type exists already
			long rowId = 0;

			
			FileInputStream fstream = new FileInputStream(mFileName);
        
			int size = fstream.available();
			
			if (fstream!=null) {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"MS949"));
				//BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)fstream,"MS949"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(fstream,"euc-kr"));
				String str;  
				//String str1[] = new String[10];  //Spelling
				String str1 = "";  // Spelling
				String str2 = "";  //Meaning
				String str3 = "";  // Extra meaning
			
				while ((str = reader.readLine()) != null) {
	
					StringTokenizer token = new StringTokenizer(str,",");
					str1 = token.nextToken();
					str2 = token.nextToken();
					str3 = "";
					while(token.hasMoreTokens())
					{
						str3 += token.nextToken();
					}

				    Voca_ManageDB.insert(mOnlyFileName,str1,str2,0,0,str3);

				}
				fstream.close();
				reader.close();
			
			}
			} catch (Exception e) {
					// Should never happen!
				Log.d("Test",Log.getStackTraceString(new Throwable()));
				handler.sendEmptyMessage(MSG_WRONG_FILE_FORMAT);
				return;
			}		
		handler.sendEmptyMessage(MSG_MOVE_TO_VOCA);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			    case MSG_FINISH_THREAD:
			    	pd.dismiss();
			    	break;
			    	
			    case MSG_MOVE_TO_VOCA:
					pd.dismiss();
					
					Intent intent = new Intent(FileBrowserActivity.this, FileList.class);
					//intent = new Intent("com.commonsware.android.label/.Drawer");
					
					startActivity(intent);
					finish();
					break;
			    case MSG_WRONG_FILE_FORMAT:
			    	pd.dismiss();
			    	Toast.makeText(FileBrowserActivity.this, "Wrong File Format", Toast.LENGTH_SHORT).show();
			    	finish();
			    	break;
				
			}

		}
	};

    /** Checks whether checkItsEnd ends with 
     * one of the Strings from fileEndings */ 
    private boolean checkEndsWithInStringArray(String checkItsEnd, 
                        String[] fileEndings){ 
         for(String aEnd : fileEndings){ 
              if(checkItsEnd.endsWith(aEnd)) 
                   return true; 
         } 
         return false; 
    } 
}