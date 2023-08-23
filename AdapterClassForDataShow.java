package com.tuition.createcontactform;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tuition.createcontactform.databinding.RecyclerViewOfcontactBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterClassForDataShow extends RecyclerView.Adapter<AdapterClassForDataShow.ViewHolder> implements Filterable {
    ArrayList<DataModelClass> dataModelClasses;
    Boolean value = false;
    Context context;
    ArrayList<DataModelClass> arrayListBackup;
    String urlImage;
    String phone;
    boolean longClick=false;
    List<DataModelClass> list = new ArrayList<>();
    public AdapterClassForDataShow(Context context, ArrayList<DataModelClass> dataModelClasses) {
        this.dataModelClasses = dataModelClasses;
        this.context = context;
        this.arrayListBackup = new ArrayList<>(dataModelClasses);

    }
    @NonNull
    @Override
    public AdapterClassForDataShow.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_ofcontact, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClassForDataShow.ViewHolder holder, int position) {

        DataModelClass dataModelClass = dataModelClasses.get(position);
        holder.binding.nameEditText.setText(dataModelClass.firstName+" "+dataModelClass.lastName);
        String name = dataModelClass.firstName;
        phone = dataModelClass.phoneNumber;
        String phoneNumber=dataModelClass.phoneNumber;
        String lName = dataModelClass.lastName;
        String company = dataModelClass.companyName;
        String calender = dataModelClass.calender;
        String email = dataModelClass.emailId;
        urlImage= dataModelClass.image;
        FormatImageWord(name, lName ,holder);


        // set image
        Bitmap bitmap;
        if (urlImage != null) {
            byte[] bytes=Base64.decode(urlImage, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            holder.binding.imagePhoto.setImageBitmap(bitmap);
            holder.binding.imagePhoto.setVisibility(View.VISIBLE);
            holder.binding.imageWord.setVisibility(View.GONE);

        }
        else
        {
            holder.binding.imageWord.setVisibility(View.VISIBLE);
            holder.binding.imagePhoto.setVisibility(View.GONE);
        }
        //set checkbox
        if (value) {
            holder.binding.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.binding.checkBox.setVisibility(View.GONE);
        }
        holder.binding.checkBox.setChecked(dataModelClass.isSelected());
        holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white));
        holder.binding.checkmate.setVisibility(View.GONE);

        if(longClick==false)
        {
            holder.itemView.setOnClickListener(v -> {
                MemberDetail(v, phoneNumber);
                Log.d("",""+phone);
                holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.white));
                holder.binding.checkmate.setVisibility(View.GONE);

            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClick=true;
                holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.lBlue));
                holder.binding.checkmate.setVisibility(View.VISIBLE);
                holder.binding.checkBox.setVisibility(View.GONE);
                holder.binding.imageWord.setVisibility(View.GONE);
                holder.binding.imagePhoto.setVisibility(View.GONE);
                list.add(dataModelClass);
                return true;
            }
        });
        holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataModelClass.setSelected(isChecked);
            Log.d("TAG", "onBindViewHolder: " + getSelectedItems().size());
        });
    }

    private void FormatImageWord(String name, String lName, ViewHolder holder) {
        String val2;
        String val = String.valueOf(name.charAt(0));
        if (!lName.equals("")) {
            String val1 = String.valueOf(lName.charAt(0));
            val2 = val + val1;
        } else {
            val2 = val;
        }
        holder.binding.imageWord.setText(val2);
        if (val.equalsIgnoreCase("A")  || val.equalsIgnoreCase("G") ||
                val.equalsIgnoreCase("J") || val.equalsIgnoreCase("M") || val.equalsIgnoreCase("P") ||
                val.equalsIgnoreCase("U") || val.equalsIgnoreCase("Z")) {
            int color= ContextCompat.getColor(context,R.color.purple_200);
            holder.binding.imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        else if (val.equalsIgnoreCase("B") || val.equalsIgnoreCase("E") || val.equalsIgnoreCase("H") ||
                val.equalsIgnoreCase("K") || val.equalsIgnoreCase("Q") ||
                val.equalsIgnoreCase("v") || val.equalsIgnoreCase("y"))
        {
            int color= ContextCompat.getColor(context,R.color.green);
            holder.binding.imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        else if (val.equalsIgnoreCase("D")  || val.equalsIgnoreCase("L") || val.equalsIgnoreCase("O") || val.equalsIgnoreCase("R") ||
                val.equalsIgnoreCase("T") || val.equalsIgnoreCase("S"))
        {
            int color= ContextCompat.getColor(context,R.color.yellow);
            holder.binding.imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        else if (val.equalsIgnoreCase("W")  || val.equalsIgnoreCase("I") || val.equalsIgnoreCase("C") || val.equalsIgnoreCase("N") ||
                val.equalsIgnoreCase("F") || val.equalsIgnoreCase("X"))
        {
            int color= ContextCompat.getColor(context,R.color.red);
            holder.binding.imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        else
        {
            int color= ContextCompat.getColor(context,R.color.white);
            holder.binding.imageWord.setBackgroundTintList(ColorStateList.valueOf(color));
        }

    }


    List<DataModelClass> getSelectedItems() {
        list.clear();
        for (DataModelClass modelClass: dataModelClasses) {
            if (modelClass.isSelected) {
                list.add(modelClass);
            }
        }
        return list;
    }
    private void MemberDetail(View v, String phone) {
        Intent intent = new Intent(v.getContext(), ShowPersonInformation.class);
        intent.putExtra("phoneNumber", phone);
        v.getContext().startActivity(intent);
        //((Activity) v.getContext()).finish();
    }

    @Override
    public int getItemCount() {
        return dataModelClasses.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DataModelClass> filteredData = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredData.addAll(arrayListBackup);
            } else {
                for (DataModelClass object : arrayListBackup) {
                    if (object.getFirstName().toString().toLowerCase().contains(constraint.toString().toLowerCase(Locale.ROOT)))
                    {
                        filteredData.add(object);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataModelClasses.clear();
            dataModelClasses.addAll((ArrayList<DataModelClass>) results.values);
            notifyDataSetChanged();
        }
    };

    public void setSelectEnable(boolean b) {
        value = b;
        if(value==false)
        {
            list.clear();
        }
        else
        {

        }
    }

    public void deleteSelectedItem() {
        Log.d("",""+list.size());
        if(!list.isEmpty())
        {
            for(int i=0; i<list.size();i++)
            {
                Log.d("",""+list.get(i).phoneNumber);
                DataStoreSqlite dataStoreSqlite= new DataStoreSqlite(context);
                dataStoreSqlite.DeleteData(list.get(i).phoneNumber);
                dataModelClasses.remove(list.get(i));
                arrayListBackup.remove(list.get(i));
            }
            Toast.makeText(context.getApplicationContext(), "Deleted",Toast.LENGTH_SHORT).show();
            value=false;
            list.clear();
            longClick=false;
            notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(context,"No contact Selected",Toast.LENGTH_SHORT).show();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerViewOfcontactBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerViewOfcontactBinding.bind(itemView);

        }
    }
}
