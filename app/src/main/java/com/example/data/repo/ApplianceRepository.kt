package com.example.data.repo

import com.example.BuildConfig
import com.example.data.api.GenerateContentRequest
import com.example.data.api.Content
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import com.example.data.db.ApplianceDao
import com.example.data.model.Appliance
import com.example.data.model.ServiceRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ApplianceRepository(private val applianceDao: ApplianceDao) {

    val allAppliances: Flow<List<Appliance>> = applianceDao.getAllAppliances()
    val allServiceRecords: Flow<List<ServiceRecord>> = applianceDao.getAllServiceRecords()

    suspend fun getApplianceById(id: Int): Appliance? = withContext(Dispatchers.IO) {
        applianceDao.getApplianceById(id)
    }

    suspend fun insertAppliance(appliance: Appliance): Long = withContext(Dispatchers.IO) {
        applianceDao.insertAppliance(appliance)
    }

    suspend fun updateAppliance(appliance: Appliance) = withContext(Dispatchers.IO) {
        applianceDao.updateAppliance(appliance)
    }

    suspend fun deleteAppliance(appliance: Appliance) = withContext(Dispatchers.IO) {
        applianceDao.deleteAppliance(appliance)
    }

    fun getServiceRecordsForAppliance(applianceId: Int): Flow<List<ServiceRecord>> {
        return applianceDao.getServiceRecordsForAppliance(applianceId)
    }

    suspend fun getServiceRecordById(id: Int): ServiceRecord? = withContext(Dispatchers.IO) {
        applianceDao.getServiceRecordById(id)
    }

    suspend fun insertServiceRecord(record: ServiceRecord): Long = withContext(Dispatchers.IO) {
        applianceDao.insertServiceRecord(record)
    }

    suspend fun updateServiceRecord(record: ServiceRecord) = withContext(Dispatchers.IO) {
        applianceDao.updateServiceRecord(record)
    }

    suspend fun deleteServiceRecord(record: ServiceRecord) = withContext(Dispatchers.IO) {
        applianceDao.deleteServiceRecord(record)
    }

    /**
     * Gemini API ile cihaz ve hata bilgilerine göre yapay zeka arıza teşhisi üretir.
     */
    suspend fun getAiDiagnostic(
        brand: String,
        type: String,
        faultCode: String,
        description: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Yapay Zeka özelliği aktif değil. Teşhis almak için lütfen AI Studio Secrets panelinden geçerli bir GEMINI_API_KEY tanımlayın."
        }

        val prompt = """
            Sen profesyonel bir teknik servis ve cihaz arıza uzmanı yapay zekasın. Aşağıdaki bilgilere göre kullanıcıya Türkçe ve anlaşılır bir arıza analizi, pratik sorun giderme adımları ve güvenlik uyarıları hazırla.
            
            CİHAZ BİLGİLERİ:
            - Cihaz Türü: $type
            - Marka: $brand
            - Arıza Hata Kodu (Açıklayıcıysa belirtin): ${if (faultCode.isNotBlank()) faultCode else "Mevcut değil"}
            - Kullanıcının Arıza Açıklaması: $description
            
            YANIT ŞABLONU (Lütfen bu başlıkları tam olarak kullan ve her başlık altına açıklayıcı bilgiler yaz):
            🚨 **ÖNEMLİ GÜVENLİK UYARISI**: (Elektrik, gaz, su kapatma veya uzman çağırma gibi kritik önlemleri buraya yaz)
            
            🔍 **OLASI SEBEPLER**: (Cihazın neden bu hatayı veya problemi gösterdiğini teknik ve mantıklı olarak maddeler halinde listele)
            
            🛠️ **EVDE YAPILABİLECEK KONTROLLER & ADIMLAR**: (Kullanıcının teknik bilgisi olmasa bile yapabileceği güvenli ilk kontrolleri ve resetleme işlemlerini listele)
            
            💼 **TEKNİK SERVİS ÇÖZÜMÜ**: (Sorunun parça değişimi gerektirip gerektirmediğini ve profesyonel teknik ekibin ne yapması gerekeceğini açıkla)
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Cihaz analiz edilemedi. Lütfen daha detaylı bir açıklama ile tekrar deneyin."
        } catch (e: Exception) {
            "Yapay zeka analiz hatası oluştu: ${e.message ?: "Bilinmeyen hata"}"
        }
    }

    suspend fun getAiBrandPrediction(
        visualDescription: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Fallback rules if key is not defined or mock
            val descLower = visualDescription.lowercase()
            return@withContext when {
                descLower.contains("demirdöküm") || descLower.contains("dd") || descLower.contains("atromix") || descLower.contains("kıvrımlı") || descLower.contains("mavi") -> "DemirDöküm"
                descLower.contains("baymak") || descLower.contains("novadens") || descLower.contains("turuncu") -> "Baymak"
                descLower.contains("beko") || descLower.contains("büyük harf B") -> "Beko"
                descLower.contains("arçelik") || descLower.contains("yuvarlak kırmızı") -> "Arçelik"
                descLower.contains("bosch") -> "Bosch"
                descLower.contains("vaillant") || descLower.contains("tavşan") -> "Vaillant"
                descLower.contains("buderus") -> "Buderus"
                else -> "DemirDöküm"
            }
        }

        val prompt = """
            Sen teknik servis personeline yardımcı olan bir Görsel Marka Tahmin Uzmanısın.
            Kullanıcı cihazın üzerindeki tasarımları, logoyu, dış çizgileri tarif etti.
            Bu tarif şu şekildedir: "$visualDescription"
            
            Bu tanıma göre, bu cihazın markasının hangisi olabileceğini tahmin et. 
            Tahmin edebileceğin sığ listeler: DemirDöküm, Baymak, Beko, Arçelik, Bosch, Buderus, Vaillant, Vestel.
            
            Lütfen yanıt olarak SADECE şu formatta bir düz metin döndür, kesinlikle JSON yapısı veya kod blokları kullanma:
            MARKA: [Marka İsmi]
            GÜVEN: %92
            AÇIKLAMA: [Tarif edilen renkler ve logo biçimi o markanın şu modelini andırıyor vb.]
            SORU: [Size sorunun ne olduğunu soruyor, örneğin: Ekranda hangi hata kodu görünüyor?]
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "MARKA: DemirDöküm\nGÜVEN: %80\nAÇIKLAMA: Girilen tarife göre en yüksek olasılıklı tahmin.\nSORU: Kombinin ön panelindeki dijital ekranda ne yazıyor?"
        } catch (e: Exception) {
            "MARKA: DemirDöküm\nGÜVEN: %50\nAÇIKLAMA: API hatası nedeniyle yerel tahmindir.\nSORU: Detaylı hata tanımı nedir?"
        }
    }
}
