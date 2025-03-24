package com.example.guitarshop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagerProfileFragment extends Fragment {
    private TextView tvManagerName, tvManagerEmail, tvManagerPhone;

    // Replace with your SQL Server details
    private static final String DB_URL = "jdbc:jtds:sqlserver://192.168.1.15:1433;databaseName=GuitarShop;user=sa;password=12345;encrypt=false";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_profile, container, false);

        tvManagerName = view.findViewById(R.id.tvName);
        tvManagerEmail = view.findViewById(R.id.tvEmail);
        tvManagerPhone = view.findViewById(R.id.tvPhone);

        new FetchManagerDetailsTask().execute(); // Fetch manager data from database

        return view;
    }

    // AsyncTask to fetch manager details from SQL Server
    private class FetchManagerDetailsTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                // Load JDBC driver
                Class.forName("net.sourceforge.jtds.jdbc.Driver");

                // Connect to database
                Connection connection = DriverManager.getConnection(DB_URL);

                // Query to get manager details (replace with your actual query)
                String query = "SELECT Name, Email, PhoneNumber FROM Seller WHERE SellerID = 1"; // Replace with actual seller ID
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return new String[]{
                            resultSet.getString("Name"),
                            resultSet.getString("Email"),
                            resultSet.getString("PhoneNumber")
                    };
                }

                connection.close();
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error fetching manager details", e);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                tvManagerName.setText("Manager Name: " + result[0]);
                tvManagerEmail.setText("Email: " + result[1]);
                tvManagerPhone.setText("Phone: " + result[2]);
            } else {
                Toast.makeText(getActivity(), "Failed to load manager details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
