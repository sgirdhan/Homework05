package com.example.sharangirdhani.homework05;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicRecyclerViewHolder> {

    ArrayList<MusicTrack> musicTrackArrayList;
    Context context;
    IMusicListAdapter iMusicListAdapter;
    List<MusicTrack> favouriteList= new ArrayList<>();

    public MusicListAdapter(ArrayList<MusicTrack> musicTrackArrayList, Context context, IMusicListAdapter iMusicListAdapter) {
        this.musicTrackArrayList = musicTrackArrayList;
        this.context = context;
        this.iMusicListAdapter=iMusicListAdapter;
    }

    @Override
    public MusicRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_row,parent,false);
        MusicRecyclerViewHolder musicRecyclerViewHolder = new MusicRecyclerViewHolder(view);
        return musicRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final MusicRecyclerViewHolder holder, final int position) {
        final MusicTrack musicTrack = musicTrackArrayList.get(position);
        if(!musicTrack.getImgSmall().isEmpty()){
            Picasso.with(context).load(musicTrack.getImgSmall()).into(holder.imageViewTrackImage);
        }

        holder.textViewTrackName.setText(musicTrack.getName());
        holder.textViewTrackArtist.setText(musicTrack.getArtist());
        holder.imageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteList = iMusicListAdapter.fetchFavouritesData(context);
                if(favouriteList.contains(musicTrack))
                {
                    iMusicListAdapter.removeDataFromFavorites(context, (ArrayList<MusicTrack>) favouriteList, musicTrack);
                    holder.imageButtonFavorite.setImageResource(R.drawable.star_silver);
                    iMusicListAdapter.refreshList();
                }

                else
                    {
                        iMusicListAdapter.addDataToFavorites(context, (ArrayList<MusicTrack>) favouriteList, musicTrack);
                        holder.imageButtonFavorite.setImageResource(R.drawable.star_gold);
                        iMusicListAdapter.refreshList();
                }
            }
        });

        favouriteList =iMusicListAdapter.fetchFavouritesData(context);

        if(favouriteList.contains(musicTrack)){
            holder.imageButtonFavorite.setImageResource(R.drawable.star_gold);
        }else{
            holder.imageButtonFavorite.setImageResource(R.drawable.star_silver);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMusicListAdapter.trackDetails(musicTrack);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicTrackArrayList.size();
    }


    public static  class MusicRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewTrackImage;
        TextView textViewTrackName;
        TextView textViewTrackArtist;
        ImageButton imageButtonFavorite;

        public MusicRecyclerViewHolder(View itemView) {
            super(itemView);
            imageViewTrackImage = (ImageView) itemView.findViewById(R.id.trackImage);
            textViewTrackName = (TextView) itemView.findViewById(R.id.trackName);
            textViewTrackArtist = (TextView) itemView.findViewById(R.id.trackArtist);
            imageButtonFavorite = (ImageButton) itemView.findViewById(R.id.imageButtonFavourite);
        }
    }

    interface IMusicListAdapter
    {
        void trackDetails(MusicTrack musicTrack);
        void refreshList();
        ArrayList<MusicTrack> fetchFavouritesData(Context context);
        void addDataToFavorites(Context context, ArrayList<MusicTrack> favouriteList, MusicTrack musicTrack);
        void removeDataFromFavorites(Context context, ArrayList<MusicTrack> favouriteList,MusicTrack musicTrack);

    }
}
