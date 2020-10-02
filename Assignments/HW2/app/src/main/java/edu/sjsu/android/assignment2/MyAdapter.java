package edu.sjsu.android.assignment2;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    // ********** Instance variables **********
    private List<int[]> values; // The dataset is a list of ID's (of strings (title & description) in R.string and pics in R.drawable)
    private Context context; // Content of the caller activity (needed for the alert button)

    // ********** Constructor **********
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<int[]> myDataset, Context context) {
        values = myDataset; // Store dataset/values
        this.context = context; // Store the context of the caller activity (needed for alert button)
    }

    // ********** Add method: Adds item to dataset **********
    public void add(int position, int[] item) {
        values.add(position, item); // Add value/item/id to dataset at index position
        notifyItemInserted(position); // Notify any registered observers that the item reflected at position has been newly inserted
    }

    // ********** Remove method: Removes item from dataset **********
    public void remove(int position) {
        values.remove(position); // Remove item/value/id at index position
        notifyItemRemoved(position); // Notify any registered observers that the item previously located at position has been removed from the data set
    }

    // ********** Create new views (invoked by the layout manager) **********
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Pass in data from MainActivity to AnimalDetailActivity
        final int[] data = values.get(position); // Extract data array for element at index = position
        // Modify how user sees elements in menu
        holder.Text.setText(data[0]); // Set title of item menu
        holder.Image.setImageResource(data[2]); // Set image of item in menu
        // Modify what happens when an element is clicked: Redirect to information page about animal
        holder.itemView.setOnClickListener(new View.OnClickListener() { // Ad an action listener (to get intent)
            @Override
            public void onClick(final View view) { // When item is clicked
                final Intent intent = new Intent(view.getContext(), AnimalDetailActivity.class); // Create intent for redirect to AnimalDetailActivity
                if (position == getItemCount() - 1) { // When the user clicks on the last (scary) animal in the animal listing activity
                    // An alert box should pop-up, warning the user that the animal is very scary and asking the user if they want to proceed
                    // Start: AlertDialog (https://developer.android.com/guide/topics/ui/dialogs)
                    AlertDialog.Builder builder = new AlertDialog.Builder(context); // Create an AlertDialog in caller activity (MainActivity)
                    builder.setTitle("Dangerous Animal"); // Set the title of the alert
                    builder.setCancelable(true); // Let the alert be cancelable
                    builder.setMessage(R.string.warning) // Display warning to user
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // For right button: Yes
                                public void onClick(DialogInterface dialog, int id) {
                                    intent.putExtra("title", data[0]);
                                    intent.putExtra("description", data[1]);
                                    intent.putExtra("image", data[2]);
                                    view.getContext().startActivity(intent); // Start the intent
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { // For left button: No
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel(); // User cancelled the dialog
                                }
                            });
                    AlertDialog alert = builder.create(); // Create the alert
                    alert.show(); // Display the alert
                    // End: AlertDialog
                } else {
                    intent.putExtra("title", data[0]);
                    intent.putExtra("description", data[1]);
                    intent.putExtra("image", data[2]);
                    view.getContext().startActivity(intent); // Start the intent
                }
            }
        });
    }

    // ********** Get the number of items in dataset **********
    @Override
    public int getItemCount() {
        return values.size();
    }

    // ********** Create list items: Each item in list has a title (TextView) and a pic (ImageView) **********
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Text;
        public ImageView Image;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            Text = (TextView) v.findViewById(R.id.firstLine);
            Image = (ImageView) v.findViewById(R.id.icon);
        }
    }
}