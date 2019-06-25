package com.atoi.touchlock.Data.Local;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.atoi.touchlock.POJO.User;
import com.atoi.touchlock.Utils.Callbacks;

public class UserRepository implements Callbacks.OnFindTaskCompleted {
    private UserDAO userDAO;
    private User mUser;

    private static UserRepository sInstance;

    private UserRepository(Application application) {
        UserRoomDB userRoomDB = UserRoomDB.getDatabase(application);
        userDAO = userRoomDB.userDAO();
    }

    public static UserRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(application);
                }
            }
        }
        return sInstance;
    }

    public void insertUser(User user, final Callbacks.OnUserInserted onUserInserted) {
        new insertAsyncTask(userDAO, onUserInserted).execute(user);
        Log.d("insertUser", "user inserted");
    }

    public User getUserByEmail(String email) {
        new findAsyncTask(userDAO, new Callbacks.OnFindTaskCompleted() {
            @Override
            public void onFindTaskCompleted(User user) {
                mUser = user;
            }
        }).execute(email);
        Log.d("findUser", "user found");
        return mUser;
    }

    @Override
    public void onFindTaskCompleted(User user) {

    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Long> {

        private UserDAO mAsyncTaskDao;
        private Callbacks.OnUserInserted onUserInserted;

        insertAsyncTask(UserDAO dao, Callbacks.OnUserInserted onUserInserted) {
            mAsyncTaskDao = dao;
            this.onUserInserted = onUserInserted;
        }


        @Override
        protected Long doInBackground(User... users) {
            long userId = mAsyncTaskDao.insert(users[0]);
            return userId;
        }

        @Override
        protected void onPostExecute(Long l) {
            onUserInserted.onUserInserted(l);
        }
    }

    private static class findAsyncTask extends AsyncTask<String, Void, User> {

        private UserDAO mAsyncTaskDao;
        private Callbacks.OnFindTaskCompleted findCallback;

        findAsyncTask(UserDAO dao, Callbacks.OnFindTaskCompleted findCallback) {
            mAsyncTaskDao = dao;
            this.findCallback = findCallback;
        }

        @Override
        protected User doInBackground(String... strings) {
            User user = mAsyncTaskDao.getUserByEmail(strings[0]);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            findCallback.onFindTaskCompleted(user);
        }
    }
}
