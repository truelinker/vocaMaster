package com.truelinker.voca_mem;
/**
 * Copyright (C) 2009 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

import android.app.Application;

/**
 * {@link Application} object for constants, global variables.
 */
public class MemoApplication extends Application {
	public   boolean checkCount ;

    public void onCreate() {
    	super.onCreate();
    	 checkCount = true;
    	
    }

    public void onTerminate() {
    	 super.onTerminate();
    }

}
