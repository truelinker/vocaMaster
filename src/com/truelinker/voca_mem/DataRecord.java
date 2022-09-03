package com.truelinker.voca_mem;

// Helper class to encapsulate Pet record data programmatically
// Allows Pet records to be compared.
public class DataRecord implements Comparable<DataRecord>{ 
    
    public static final long INVALID_FILE_ID = -1;
    //public static final long INVALID_FILE_IMAGE_ID = -1;
    public String mName; 
    //private String mFileType; 
    //private String mPetImageUriPath; 
    //private long mPetImageId = INVALID_PET_IMAGE_ID; //(Uri tag)
    public long mId = INVALID_FILE_ID; //(Database unique pk record id)
    
    private boolean mSelectable = true; 

    public DataRecord(String Name, Long fileId) { 
	     mName = Name; 
	      
	     mId = fileId; // this will be INVALID_PET_ID for unsaved records
    } 
     
    public boolean isSelectable() { 
         return mSelectable; 
    } 
     
    public void setSelectable(boolean selectable) { 
         mSelectable = selectable; 
    } 
     
    public String getName() { 
         return mName; 
    } 
     
    public void setName(String text) { 
   	 mName = text; 
    } 
    /*
    public String getFileImageUriPath() { 
         return mFileImageUriPath; 
    } 
     
    public void setPetImageUriPath(String text) { 
   	 mPetImageUriPath = text; 
    } 
    
    public String getPetType() { 
         return mPetType; 
    } 
     
    public void setPetType(String text) { 
   	 mPetType = text; 
    } 
     
    public long getPetImageId() { 
         return mPetImageId; 
    } 
     
    public void setPetImageId(long id) { 
   	 mPetImageId = id; 
    } 
    */
    public long getId() { 
         return mId; 
    } 
     
    public void setFileId(long id) { 
   	 mId = id; 
    } 
    
    public int compareTo(DataRecord other) { 
         return (int)((this.mId)-(other.mId));
    } 
} 



