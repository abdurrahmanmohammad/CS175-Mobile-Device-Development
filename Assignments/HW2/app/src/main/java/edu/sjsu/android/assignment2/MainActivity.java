package edu.sjsu.android.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(createList(), this);
        recyclerView.setAdapter(mAdapter);
    }

    public List<int[]> createList() {
        final List<int[]> input = new ArrayList<>();
        int[] peacock = {R.string.peacock, R.string.peacockDescription, R.drawable.peacock};
        input.add(peacock);
        int[] penguin = {R.string.penguin, R.string.penguinDescription, R.drawable.penguin};
        input.add(penguin);
        int[] zebra = {R.string.zebra, R.string.zebraDescription, R.drawable.zebra};
        input.add(zebra);
        int[] bear = {R.string.bear, R.string.bearDescription, R.drawable.bear};
        input.add(bear);
        int[] lion = {R.string.lion, R.string.lionDescription, R.drawable.lion};
        input.add(lion);
        return input;
    }

    // ********** The 2 methods below create the menu (Information, Uninstall) **********
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.information: // View information
                // Intent: Redirect to ZooInformationActivity
                Intent myIntent = new Intent(this, ZooInformationActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.uninstall: // Uninstall application
                // Intent: Uninstall application
                Uri packageURI = Uri.parse("package:" + getApplicationContext().getPackageName()); // Uninstall package
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}