package se.miun.holi1900.dt031g.dialer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.miun.holi1900.dt031g.dialer.database.CallInfo;

public class CallInfoAdapter extends RecyclerView.Adapter<CallInfoAdapter.ViewHolder>{
    private List<CallInfo> callData;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private final TextView phoneNumberTextView;
        private final TextView dateTextView;
        private final TextView locationTextView;
        private String locationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumberTextView = itemView.findViewById(R.id.phone_number_textView);
            dateTextView = itemView.findViewById(R.id.date_textView);
            locationTextView = itemView.findViewById(R.id.location_textView);
            locationText = itemView.getResources().getString(R.string.call_location_varable_string);
        }

        /**
         * gets the textview to display phone number
         * @return textView
         */
        TextView getPhoneNumberTextView(){
            return phoneNumberTextView;
        }

        /**
         * gets the textview to display date of phone call
         * @return textView
         */
        TextView getDateTextView(){
            return dateTextView;
        }

        /**
         * gets the textview to display location of phone call
         * @return TextView
         */
        TextView getLocationTextView(){
            return locationTextView;
        }

        /**
         * Gets the values of latitude and longitude and formats
         * it in to the location text to be displayed
         * @param latitude latitude
         * @param longitude longitude
         * @return location text to be displayed
         */
        String getLocationText(double latitude, double longitude){
            return String.format(locationText, latitude, longitude);
        }
    }

    public CallInfoAdapter(List<CallInfo> callData) {
        this.callData = callData;
    }

    @NonNull
    @Override
    public CallInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_info_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallInfoAdapter.ViewHolder holder, int position) {
        holder.getPhoneNumberTextView().setText(callData.get(position).phoneNumber);
        holder.getDateTextView().setText(callData.get(position).date);
        double lat = callData.get(position).latitude;
        double lon = callData.get(position).longitude;

        if(lat!=0 || lon!=0){
            holder.getLocationTextView().setText(holder.getLocationText(lat, lon));
        }
        else{
            holder.getLocationTextView().setText("[??, ??]");
        }
    }

    @Override
    public int getItemCount() {
        return callData.size();
    }
}
