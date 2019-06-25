package com.atoi.touchlock.Utils;

import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.POJO.User;

import java.util.List;

public interface Callbacks {

    interface OnFindTaskCompleted{
        void onFindTaskCompleted(User user);
    }

    interface OnAdvertisementFindCompleted {
        void onAdvertisementFindCompleted(List<Advertisement> advertisement);
    }

    interface OnUserInserted {
        void onUserInserted(Long userId);
    }
}
