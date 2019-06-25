package com.atoi.touchlock.Data.Local;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.Utils.Callbacks;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementRepository{
    private AdvertisementDAO advertisementDAO;
    private List<Advertisement> mAdvertisements;

    private static AdvertisementRepository sInstance;

    private AdvertisementRepository(Application application) {
        UserRoomDB userRoomDB = UserRoomDB.getDatabase(application);
        advertisementDAO = userRoomDB.advertisementDAO();
        mAdvertisements = new ArrayList<>();
    }

    public static AdvertisementRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (AdvertisementRepository.class) {
                if (sInstance == null) {
                    sInstance = new AdvertisementRepository(application);
                }
            }
        }
        return sInstance;
    }

    public void deleteAdvertisement(Advertisement advertisement) {
        new AdvertisementRepository.deleteAsyncTask(advertisementDAO).execute(advertisement);
        Log.d("deleteAd", "ad deleted");
    }

    public void insertAdvertisement(Advertisement advertisement) {
        new AdvertisementRepository.insertAsyncTask(advertisementDAO).execute(advertisement);
        Log.d("insertAd", "ad inserted");
    }

    public List<Advertisement> getAdByEmail(String email, final Callbacks.OnAdvertisementFindCompleted callback) {
        new AdvertisementRepository.findAsyncTask(advertisementDAO, callback).execute(email);
        Log.w("findAd", "Ad found");
        return mAdvertisements;
    }


    private static class insertAsyncTask extends AsyncTask<Advertisement, Void, Void> {

        private AdvertisementDAO mAsyncTaskDao;

        insertAsyncTask(AdvertisementDAO dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Advertisement... advertisements) {
            mAsyncTaskDao.insert(advertisements[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Advertisement, Void, Void> {

        private AdvertisementDAO mAsyncTaskDao;

        deleteAsyncTask(AdvertisementDAO dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Advertisement... advertisements) {
            mAsyncTaskDao.delete(advertisements[0]);
            return null;
        }
    }

    private static class findAsyncTask extends AsyncTask<String, Void, List<Advertisement>> {

        private AdvertisementDAO mAsyncTaskDao;
        private Callbacks.OnAdvertisementFindCompleted callback;
        findAsyncTask(AdvertisementDAO dao, Callbacks.OnAdvertisementFindCompleted callback) {
            mAsyncTaskDao = dao;
            this.callback = callback;
        }

        @Override
        protected List<Advertisement> doInBackground(String... strings) {
            List<Advertisement> advertisements = mAsyncTaskDao.findAdsForUser(strings[0]);
            return advertisements;
        }

        @Override
        protected void onPostExecute(List<Advertisement> advertisements) {
            callback.onAdvertisementFindCompleted(advertisements);
        }
    }
}
