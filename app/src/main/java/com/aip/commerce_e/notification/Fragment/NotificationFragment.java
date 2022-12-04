package com.aip.commerce_e.notification.Fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentNotificationBinding;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class NotificationFragment extends Fragment implements NotificationInterface{

    private FragmentNotificationBinding binding;
    NotificationListAdapter notificationListAdapter;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        notificationListAdapter = new NotificationListAdapter(null, this);
        // add list of notification to adapter
        notificationListAdapter.setNotifications(MainActivity.notifications);
        binding.notificationRcv.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.notificationRcv.setAdapter(notificationListAdapter);

        return binding.getRoot();
    }

    public void onViewCreated(@NotNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void navigateOnCLick(Integer pos) {

    }
}