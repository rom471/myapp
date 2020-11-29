package com.rom471.db2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


@Database(entities = {App.class,OneUse.class},version = 4,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private static MyDataBase INSTANCE;//单例模式
    private static final Object Lock = new Object();
    public abstract MyDao appDao();
    public static MyDao getAppDao(){
        return INSTANCE.appDao();
    }
    public static MyDataBase getInstance(Context context){
        synchronized (Lock){
            if(INSTANCE==null){
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), MyDataBase.class, "apps.db")
                                .allowMainThreadQueries()
                                .addMigrations(MIGRATION_Add_)
                                .build();
            }
            return INSTANCE;
        }
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    static Migration MIGRATION_Add_= new Migration(3,4 ) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("delete from OnePred ");
        }
    };
}
