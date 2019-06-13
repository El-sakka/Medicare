package com.sakkawy.medicare.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Folder implements Parcelable {
    String parent;
    String userId;
    String folderName;
    String folderId;

    public Folder() {
    }

    public Folder(String parent, String userId, String folderName,String folderId) {
        this.parent = parent;
        this.userId = userId;
        this.folderName = folderName;
        this.folderId = folderId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    protected Folder(Parcel in) {
        parent = in.readString();
        userId = in.readString();
        folderName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parent);
        dest.writeString(userId);
        dest.writeString(folderName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
