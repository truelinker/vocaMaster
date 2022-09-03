package com.truelinker.service;

import android.provider.BaseColumns;

//FYI: This is the same setup as PetTracker, but we're adding a few fields for storing image URis
public final class Voca_DB {

    private Voca_DB() {}
    
    // voca table
    public static final class VocaItems implements BaseColumns {

        private VocaItems() {}
        
        public static final String VOCA_ITEM_TABLE_NAME = "vocaItems";
        
        public static final String ITEM_NAME = "spell";
        public static final String ITEM_MEAN = "mean";
        public static final String ITEM_INFO = "extra_mean";
        public static final String ITEM_MEMO_STEP = "memo_step";
        public static final String ITEM_MEMO_DATE = "memo_date";
        public static final String ITEM_FILE_ID = "file_id";
        public static final String ITEM_CORRECT_NUM = "correct_num";
        public static final String ITEM_WRONG_NUM = "wrong_num";
        public static final String ITEM_RATING = "rating";
        //public static final String PET_IMAGE_URI = "pet_image_uri";	// new column for Uri path (without id)
        //public static final String PET_IMAGE_ID = "pet_image_id";	// new column for Uri id on SD card
        
        public static final String DEFAULT_SORT_ORDER = "spell ASC";
    }
    
    
    
    // File table
    public static final class FileItems implements BaseColumns {

        private FileItems() {}
        
        public static final String FILE_TABLE_NAME = "fileItems";
        
        public static final String FILE_NAME = "filename";
        
        public static final String DEFAULT_SORT_ORDER = "filename ASC";
    }
}