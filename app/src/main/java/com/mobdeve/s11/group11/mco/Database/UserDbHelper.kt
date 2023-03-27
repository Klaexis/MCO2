package com.mobdeve.s11.group11.mco.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class UserDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    // The singleton pattern design
    companion object {
        private var instance: UserDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): UserDbHelper? {
            if (instance == null) {
                instance = UserDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
    }

    // Called when a new version of the DB is present; hence, an "upgrade" to a newer version
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        onCreate(sqLiteDatabase)
    }

    @Synchronized
    fun getUser(email: String) : User{
        val database = this.readableDatabase
        val whereClause = "email=?"
        val whereArgs = arrayOf<String>(email)

        var user: User = User("", "", 0, "", "", 0)

        //val userCursor : Cursor = database.rawQuery("SELECT * FROM " + DbReferences.TABLE_NAME + "WHERE email=" + email, null)
        val userCursor : Cursor = database.query(
            DbReferences.TABLE_NAME,
            arrayOf("first_name", "last_name", "weight", "email", "password", "id"),
            whereClause,
            whereArgs,
            null, null, null
        )

        if(userCursor.count > 0) {
            userCursor.moveToFirst()
            user = User(
                userCursor.getString(userCursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                userCursor.getString(userCursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                userCursor.getInt(userCursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_WEIGHT)),
                userCursor.getString(userCursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_EMAIL)),
                userCursor.getString(userCursor.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                userCursor.getLong(userCursor.getColumnIndexOrThrow(DbReferences._ID))
            )
        }

        userCursor.close()
        database.close()
        return user
    }

    @Synchronized
    fun checkLogin(email: String, password: String) : Boolean{
        val database = this.writableDatabase
        val whereClause = "email=? AND password=?"
        val whereArgs = arrayOf<String>(email, password)

        val loginCursor : Cursor = database.query(
            DbReferences.TABLE_NAME,
            arrayOf("email", "password"),
            whereClause,
            whereArgs,
            null, null, null
        )

        var isTrue = if(loginCursor.count == 1){
            loginCursor.moveToFirst()
            true
        } else {
            loginCursor.moveToFirst()
            false
        }

        loginCursor.close()
        database.close()

        return isTrue
    }

    @Synchronized
    fun checkEmailExist(email: String) : Boolean{
        val database = this.writableDatabase
        val whereClause = "email=?"
        val whereArgs = arrayOf<String>(email)

        val emailCursor : Cursor = database.query(
            DbReferences.TABLE_NAME,
            arrayOf("email"),
            whereClause,
            whereArgs,
            null, null, null
        )

        var isExist = if(emailCursor.count > 0){
            emailCursor.moveToFirst()
            true
        } else {
            emailCursor.moveToFirst()
            false
        }
        
        emailCursor.close()
        database.close()
        
        return isExist
    }

    @Synchronized
    fun addUser(user: User) {
        val database = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DbReferences.COLUMN_NAME_LAST_NAME, user.lastName)
        values.put(DbReferences.COLUMN_NAME_FIRST_NAME, user.firstName)
        values.put(DbReferences.COLUMN_NAME_WEIGHT, user.weight)
        values.put(DbReferences.COLUMN_NAME_EMAIL, user.email)
        values.put(DbReferences.COLUMN_NAME_PASSWORD, user.password)

        // Insert the new row
        // Inserting returns the primary key value of the new row, but we can ignore that if we don’t need it
        database.insert(DbReferences.TABLE_NAME, null, values)
        database.close()
    }

    fun getAllUserDefault(): ArrayList<User>  {
        val database: SQLiteDatabase = this.readableDatabase

        val c : Cursor = database.query(
            DbReferences.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            DbReferences.COLUMN_NAME_LAST_NAME + " ASC, " + DbReferences.COLUMN_NAME_FIRST_NAME + " ASC",
            null
        )

        val user : ArrayList<User>  = ArrayList()

        while(c.moveToNext()) {
            user.add(User(
                c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                c.getInt(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_WEIGHT)),
                c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_EMAIL)),
                c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_PASSWORD)),
                c.getLong(c.getColumnIndexOrThrow(DbReferences._ID))
            ))
        }

        c.close()
        database.close()

        return user
    }

    private object DbReferences {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "User_Database.db"

        const val TABLE_NAME = "User"
        const val _ID = "id"
        const val COLUMN_NAME_FIRST_NAME = "first_name"
        const val COLUMN_NAME_LAST_NAME = "last_name"
        const val COLUMN_NAME_WEIGHT = "weight"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD = "password"

        const val CREATE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_FIRST_NAME + " TEXT, " +
                    COLUMN_NAME_LAST_NAME + " TEXT, " +
                    COLUMN_NAME_WEIGHT + " TEXT, " +
                    COLUMN_NAME_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_NAME_PASSWORD + " TEXT)"

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}