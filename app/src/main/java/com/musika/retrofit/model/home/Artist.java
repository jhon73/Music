package com.musika.retrofit.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist {

@SerializedName("idu")
@Expose
private Integer idu;
@SerializedName("realname")
@Expose
private String realname;

public Integer getIdu() {
return idu;
}

public void setIdu(Integer idu) {
this.idu = idu;
}

public String getRealname() {
return realname;
}

public void setRealname(String realname) {
this.realname = realname;
}

}