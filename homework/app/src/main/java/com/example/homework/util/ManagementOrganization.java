package com.example.homework.util;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.homework.R;
import com.example.homework.util.Organization;
import com.example.homework.util.OrganizationAdapter;
import com.example.homework.util.OrganizationDatabase;
import java.util.ArrayList;
import java.util.List;
// Fragment of showing Organization with add remove and edit buttons
public class ManagementOrganization extends Fragment {

    private int selectedPosition = -1;
    OrganizationAdapter adapter2;
    ArrayAdapter<String> adapter;

    // view the viewlist using the database fire store and sqliite and adapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.managmentorganaztion, container, false);

        Intent i = requireActivity().getIntent();
        OrganizationDatabase db2 = new OrganizationDatabase(requireActivity());
        db2.getAllOrganization3();
        Cursor cursor = db2.getAllOrganization();

        List<Organization> OrganizationList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {                //full in all the list
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                Organization Organization = new Organization(name, des);
                OrganizationList.add(Organization);
            } while (cursor.moveToNext());
        }

         adapter2 = new OrganizationAdapter(requireActivity(), android.R.layout.simple_list_item_1, OrganizationList);
        ListView listViewDonors = view.findViewById(R.id.listViewOrganizaton);
        listViewDonors.setAdapter(adapter2);

        ListView listView = view.findViewById(R.id.listViewOrganizaton);
        OrganizationDatabase db = new OrganizationDatabase(requireActivity());
        List<String> itemList = db.getAllOrganization2();
         adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        OrganizationDatabase db3 = new OrganizationDatabase(requireActivity() ,adapter);



        listViewDonors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
            }
        });
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {   // go to add page
            @Override
            public void onClick(View v) {
                AddOrganaztion(v);
            }
        });
        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {   // go to edit page
            @Override
            public void onClick(View v) {
                Editorg(v);
            }
        });
        Button remove = view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() { // remove button
            @Override
            public void onClick(View v) {
                removeSelectedItem(v);
            }
        });

        return view;
    }
    // remove selected item
    public void removeSelectedItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() { // confirm deleting
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedPosition != -1) {
                            removeItem(selectedPosition);
                            adapter.remove(adapter.getItem(selectedPosition));
                            adapter.notifyDataSetChanged();
                            selectedPosition = -1;
                            //confirm delete
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //cancel deleting
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel button
                    }
                })
                .setCancelable(false)
                .show();
    }
    // remove item from the list
    private void removeItem(int position) {
        OrganizationDatabase db = new OrganizationDatabase(requireActivity());
        Cursor cursor = db.getAllOrganization();
        if (cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            db.DeleteOrganization(id,getView());
            cursor.close();
        }
    }
    // button go to add  new Organaztion
    public void AddOrganaztion(View v) {
        Intent intent = new Intent(requireActivity(), AddOrgaization.class);
        startActivity(intent);
    }
    // button go edit Organaztion
    public void Editorg(View v) {
        if (selectedPosition != -1) {
            OrganizationDatabase db = new OrganizationDatabase(requireActivity());
            Cursor cursor = db.getAllOrganization();
            if (cursor != null && cursor.moveToPosition(selectedPosition)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
              //  db.DeleteOrganization(id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                cursor.close();
                Intent intent = new Intent(requireActivity(), EditOrgnization.class);
                intent.putExtra("Name", name);
                intent.putExtra("des", des);
                intent.putExtra("id",id);

              //  adapter.remove(adapter.getItem(selectedPosition));
                adapter2.notifyDataSetChanged();
                selectedPosition = -1;
                startActivity(intent);
            }
        }
    }
}
