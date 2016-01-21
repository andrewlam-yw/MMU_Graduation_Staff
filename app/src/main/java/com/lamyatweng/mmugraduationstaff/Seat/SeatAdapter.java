package com.lamyatweng.mmugraduationstaff.Seat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lamyatweng.mmugraduationstaff.R;

public class SeatAdapter extends ArrayAdapter<Seat> {

    Context mContext;

    public SeatAdapter(Context mContext) {
        super(mContext, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {
            // inflate layout
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_seat, parent, false);
            // set up ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.textViewRow = (TextView) convertView.findViewById(R.id.text_view_row);
            viewHolder.textViewColumn = (TextView) convertView.findViewById(R.id.text_view_column);
            viewHolder.itemSeatContainer = (LinearLayout) convertView.findViewById(R.id.item_seat_container);

            // store holder with view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        Seat seat = getItem(position);

        if (seat != null) {
            viewHolder.textViewRow.setText(seat.getRow());
            viewHolder.textViewColumn.setText(seat.getColumn());

            switch (seat.getStatus()) {
                case "available":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatAvailable));
                    break;
                case "occupied":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatOccupied));
                    break;
                case "disabled":
                    viewHolder.itemSeatContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.seatDisabled));
                    break;
            }
        }

        return convertView;
    }
}

class ViewHolderItem {
    TextView textViewRow;
    TextView textViewColumn;
    LinearLayout itemSeatContainer;
}
