package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.Appliance
import com.example.data.model.ServiceRecord
import com.example.data.repo.ApplianceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface DiagnosticUiState {
    object Idle : DiagnosticUiState
    object Loading : DiagnosticUiState
    data class Success(val diagnostic: String) : DiagnosticUiState
    data class Error(val message: String) : DiagnosticUiState
}

class ServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ApplianceRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ApplianceRepository(database.applianceDao)
        
        // Veritabanı boşsa başlangıç için örnek bir iki cihaz ve arıza ekleyelim.
        // Kullanıcıya kolaylık sağlar.
        viewModelScope.launch {
            repository.allAppliances.first().let { list ->
                if (list.isEmpty()) {
                    val compId1 = repository.insertAppliance(
                        Appliance(
                            type = "Kombi",
                            brand = "Baymak",
                            model = "Novadens Extra 24",
                            serialNumber = "BM-99881122",
                            serviceAddress = "Oturma Odası Balkon",
                            contactName = "Ahmet Yılmaz",
                            contactPhone = "0532 111 22 33"
                        )
                    )
                    repository.insertServiceRecord(
                        ServiceRecord(
                            applianceId = compId1.toInt(),
                            title = "Kombi Sıcak Su Vermiyor",
                            description = "Sıcak su musluğu açıldığında cihaz çalışmaya çalışıyor ancak 3 saniye sonra kilitlenip kırmızı ışık yakıyor.",
                            faultCode = "E03",
                            status = "Açık",
                            reportedDate = System.currentTimeMillis() - 86400000 * 2,
                            aiDiagnostic = "🚨 **ÖNEMLİ GÜVENLİK UYARISI**: Kombide gaz veya basınç hatası durumunda müdahale etmeden önce ana gaz vanasını kontrol edin ama cihazın içini açmayın.\n\n🔍 **OLASI SEBEPLER**: Baymak Kombi E03 hatası genellikle hava akış anahtarı (prosestat) veya baca tıkanıklığı nedeniyle çekim yetersizliğinden kaynaklanır.\n\n🛠️ **EVDE YAPILABİLECEK KONTROLLER**: 1. Kombi bacasının dış çıkışını kontrol edin, tıkayan poşet vs. olmadığını görün. 2. Cihazı reset tuşuna 3 saniye basarak yeniden başlatın."
                        )
                    )

                    val compId2 = repository.insertAppliance(
                        Appliance(
                            type = "Buzdolabı",
                            brand = "Beko",
                            model = "No-Frost XL",
                            serialNumber = "BK-11223344",
                            serviceAddress = "Mutfak",
                            contactName = "Ahmet Yılmaz",
                            contactPhone = "0532 111 22 33"
                        )
                    )
                    repository.insertServiceRecord(
                        ServiceRecord(
                            applianceId = compId2.toInt(),
                            title = "Alt tarafta buzlanma var",
                            description = "Sebzelik bölümünde aşırı soğuma yapıyor, şişeler donuyor.",
                            faultCode = "",
                            status = "İşlemde",
                            reportedDate = System.currentTimeMillis() - 86400000
                        )
                    )
                }
            }
        }
    }

    // Cihazlar akışı
    val appliances: StateFlow<List<Appliance>> = repository.allAppliances
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Tüm servis kayıtları akışı
    val allServiceRecords: StateFlow<List<ServiceRecord>> = repository.allServiceRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Yapay Zeka Teşhis Durumu
    private val _diagnosticState = MutableStateFlow<DiagnosticUiState>(DiagnosticUiState.Idle)
    val diagnosticState: StateFlow<DiagnosticUiState> = _diagnosticState.asStateFlow()

    fun insertAppliance(appliance: Appliance, onSuccess: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertAppliance(appliance)
            onSuccess(id)
        }
    }

    fun updateAppliance(appliance: Appliance) {
        viewModelScope.launch {
            repository.updateAppliance(appliance)
        }
    }

    fun deleteAppliance(appliance: Appliance) {
        viewModelScope.launch {
            repository.deleteAppliance(appliance)
        }
    }

    fun getServiceRecordsForAppliance(applianceId: Int): Flow<List<ServiceRecord>> {
        return repository.getServiceRecordsForAppliance(applianceId)
    }

    fun insertServiceRecord(record: ServiceRecord, onSuccess: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertServiceRecord(record)
            onSuccess(id)
        }
    }

    fun updateServiceRecord(record: ServiceRecord) {
        viewModelScope.launch {
            repository.updateServiceRecord(record)
        }
    }

    fun deleteServiceRecord(record: ServiceRecord) {
        viewModelScope.launch {
            repository.deleteServiceRecord(record)
        }
    }

    fun diagnoseServiceRecord(
        recordId: Int,
        brand: String,
        type: String,
        faultCode: String,
        description: String,
        onComplete: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _diagnosticState.value = DiagnosticUiState.Loading
            val result = repository.getAiDiagnostic(brand, type, faultCode, description)
            _diagnosticState.value = DiagnosticUiState.Success(result)
            
            // Veritabanındaki kaydı güncelleyelim
            val existing = repository.getServiceRecordById(recordId)
            if (existing != null) {
                repository.updateServiceRecord(existing.copy(aiDiagnostic = result))
            }
            onComplete(result)
        }
    }

    fun clearDiagnosticState() {
        _diagnosticState.value = DiagnosticUiState.Idle
    }

    private val _brandPredictionState = MutableStateFlow<String>("")
    val brandPredictionState: StateFlow<String> = _brandPredictionState.asStateFlow()

    private val _isPredictingBrand = MutableStateFlow(false)
    val isPredictingBrand: StateFlow<Boolean> = _isPredictingBrand.asStateFlow()

    fun predictBrand(visualDescription: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isPredictingBrand.value = true
            _brandPredictionState.value = ""
            val result = repository.getAiBrandPrediction(visualDescription)
            _brandPredictionState.value = result
            _isPredictingBrand.value = false
            onComplete()
        }
    }

    fun clearBrandPrediction() {
        _brandPredictionState.value = ""
        _isPredictingBrand.value = false
    }
}
