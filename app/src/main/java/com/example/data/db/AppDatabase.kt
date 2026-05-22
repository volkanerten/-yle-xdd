package com.example.data.db

import android.content.Context
import androidx.room.*
import com.example.data.model.Appliance
import com.example.data.model.ServiceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplianceDao {
    @Query("SELECT * FROM appliances ORDER BY id DESC")
    fun getAllAppliances(): Flow<List<Appliance>>

    @Query("SELECT * FROM appliances WHERE id = :id")
    suspend fun getApplianceById(id: Int): Appliance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppliance(appliance: Appliance): Long

    @Update
    suspend fun updateAppliance(appliance: Appliance)

    @Delete
    suspend fun deleteAppliance(appliance: Appliance)

    // Servis / Arıza Kayıtları sorguları
    @Query("SELECT * FROM service_records ORDER BY reportedDate DESC")
    fun getAllServiceRecords(): Flow<List<ServiceRecord>>

    @Query("SELECT * FROM service_records WHERE applianceId = :applianceId ORDER BY reportedDate DESC")
    fun getServiceRecordsForAppliance(applianceId: Int): Flow<List<ServiceRecord>>

    @Query("SELECT * FROM service_records WHERE id = :id")
    suspend fun getServiceRecordById(id: Int): ServiceRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceRecord(record: ServiceRecord): Long

    @Update
    suspend fun updateServiceRecord(record: ServiceRecord)

    @Delete
    suspend fun deleteServiceRecord(record: ServiceRecord)
}

@Database(entities = [Appliance::class, ServiceRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val applianceDao: ApplianceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "service_app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
