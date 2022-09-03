package com.truelinker.voca_mem;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class MultiChoiceData implements Parcelable {

	public String mSpell;
	public String mMean;
	
	public MultiChoiceData(){
		
	}
	
	public MultiChoiceData(String spell, String mean){
		this.mSpell = spell;
		this.mMean = mean;
	}
	
	public MultiChoiceData(Parcel src){
		Bundle objectBundle = src.readBundle();
		this.mSpell = objectBundle.getString("spell");
		this.mMean = objectBundle.getString("mean");
	}
	
	public String getSpell() {
		return mSpell;
	}
	
	public String getMean() {
		return mMean;
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		Bundle objectBundle = new Bundle();
		objectBundle.putString("spell", this.mSpell);
		objectBundle.putString("mean", this.mMean);
		dest.writeBundle(objectBundle);
		
	}
	
	public static final  Parcelable.Creator<MultiChoiceData> CREATOR = new Parcelable.Creator<MultiChoiceData>() {
		public MultiChoiceData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MultiChoiceData(source);
		}
		
		public MultiChoiceData[] newArray(int size) {
			return new MultiChoiceData[size];
		}
	};
			
}