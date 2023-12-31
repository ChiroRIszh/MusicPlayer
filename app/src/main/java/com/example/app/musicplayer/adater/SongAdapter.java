package com.example.app.musicplayer.adater;


import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/*import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;*/

import com.example.app.musicplayer.R;
import com.example.app.musicplayer.ServiceImpl.MusicDownloadService;
import com.example.app.musicplayer.dto.SongDto;
import com.example.app.musicplayer.entity.SongBean;
import com.example.app.musicplayer.entity.SongSheetBean;

import org.litepal.LitePal;

import java.io.File;

public class SongAdapter extends BaseAdapter {
    private static final String TAG = "SongAdapter";
    private SongDto songDto;
    private Context mContext;

    public SongAdapter(Context context, SongDto songDto){
        this.songDto = songDto;
        this.mContext = context;
    }

    public SongAdapter(Context context){
        this.songDto = new SongDto(LitePal.findFirst(SongSheetBean.class),LitePal.findAll(SongBean.class));
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return songDto.getSongBeanList().size();
    }

    @Override
    public Object getItem(int i) {
        return songDto.getSongBeanList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongBean songBean = (SongBean) getItem(i);
        View contentView;
        ViewHolder viewHolder;
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_song, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = contentView.findViewById(R.id.item_song_info);
            viewHolder.menu = contentView.findViewById(R.id.item_song_menu);
            contentView.setTag(viewHolder);
        } else {
            contentView = view;
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textView.setText(songBean.getName());
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopMenu(view);
                    }
                });
            }
        });
        viewHolder.menu.setTag(songBean);
        return contentView;
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView menu;
    }

    private void showPopMenu(final View view) {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_addtosheet, null);
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        final SongBean songBean = (SongBean) view.getTag();
        final SongAdapter songAdapter = this;
        popupMenu.getMenuInflater().inflate(R.menu.song_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_song_download) {
                    Toast.makeText(mContext, "下载歌曲中！", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(mContext, "下载成功！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        popupMenu.show();
    }



}
