package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appliances")
data class Appliance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,          // Kombi, Buzdolabı, Televizyon, Fırın, Pelet, vb.
    val brand: String,         // Beko, Arçelik, Demirdöküm, Baymak, vb.
    val model: String = "",
    val serialNumber: String = "",
    val installationDate: Long = System.currentTimeMillis(),
    val serviceAddress: String = "", // Konum/Adres
    val contactName: String = "",    // Müşteri / Sahip ismi
    val contactPhone: String = ""    // İletişim Telefonu
)

@Entity(tableName = "service_records")
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val applianceId: Int,      // İlişkili cihaz ID
    val title: String,         // Arıza tanımı (Örn: "Isıtmıyor", "Su sızdırıyor")
    val description: String = "",
    val faultCode: String = "", // Hata Kodu (Örn: "F1", "E03")
    val status: String = "Açık", // "Açık", "İşlemde", "Çözüldü", "İptal"
    val reportedDate: Long = System.currentTimeMillis(),
    val serviceDate: Long = 0,   // Servis randevu veya çözüm tarihi
    val cost: Double = 0.0,
    val technicianNotes: String = "",
    val aiDiagnostic: String = "" // Yapay Zeka Teşhisi ve Çözüm Önerileri
)
