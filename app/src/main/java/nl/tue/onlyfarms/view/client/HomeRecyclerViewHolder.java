package nl.tue.onlyfarms.view.client;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import nl.tue.onlyfarms.R;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView nameField;
    private TextView addressField;
    private TextView openingsHoursField;
    private TextView ratingField;
    private TextView tagsField;
    private ImageView imageField;

    public HomeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameField = itemView.findViewById(R.id.storeCard_nameAndDistanceField);
        addressField = itemView.findViewById(R.id.storeCard_addressField);
        openingsHoursField = itemView.findViewById(R.id.storeCard_OpeningHoursField);
        ratingField = itemView.findViewById(R.id.storeCard_RatingField);
        tagsField = itemView.findViewById(R.id.storeCard_TagsField);
        imageField = itemView.findViewById(R.id.storeCard_imageView);
    }

    public TextView getNameField() { return nameField; }

    public TextView getAddressField() { return addressField; }

    public TextView getOpeningsHoursField() {return openingsHoursField; }

    public TextView getRatingField() { return ratingField; }

    public TextView getTagsField() { return tagsField; }

    public ImageView getImageField() { return imageField; }
}
