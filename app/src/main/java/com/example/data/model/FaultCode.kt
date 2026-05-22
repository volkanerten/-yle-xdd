package com.example.data.model

data class FaultCode(
    val brand: String,         // Beko, Arçelik, DemirDöküm, Baymak, Bosch, Vestel, Buderus, Vaillant
    val type: String,          // Kombi, Pelet, Buzdolabı, Fırın, Televizyon, Çamaşır Makinesi
    val code: String,          // F01, E03, vb.
    val title: String,         // Hata kısa tanımı
    val reason: String,        // Neden olduğu (Why it happened)
    val part: String,          // Değişmesi gereken parça (Defective component)
    val installation: String   // Nasıl takıldığı/tamir edildiği
)

object FaultDatabase {
    val list = listOf(
        // === DEMİRDÖKÜM KOMBİ ===
        FaultCode(
            brand = "DemirDöküm",
            type = "Kombi",
            code = "F01",
            title = "Aşırı Isınma Arızası (Limit Termostat)",
            reason = "Kombi içerisindeki su sıcaklığının 98°C'yi aşması sonucu limit termostat devreyi kesmiştir. Genellikle sirkülasyon pompası sıkışması veya kalorifer vanalarının kapalı olmasından kaynaklanır.",
            part = "Limit Termostat, Sirkülasyon Pompası, Ana Eşanjör",
            installation = "1. Kombi elektriğini ana sigortadan kapatın.\n2. Kombinin ön kapağını vidalarını sökerek çıkarın.\n3. Isı emniyetini sağlayan limit termostatı (bakır boru üzerinde yer alır) tespit edin.\n4. Termostat kablo uçlarındaki klemens soketlerini çekerek çıkarın.\n5. Arızalı termostatı borudan vidalarını gevşeterek sökün.\n6. Yeni termostatın altına ısı iletken macun (termal macun) sürün ve aynı yere sıkıca monte edin.\n7. Kabloları geri bağlayın.\n8. Kalorifer vanalarını açın, sirkülasyon pompasının arkasındaki düz vidayı gevşetip mili kontrol ederek sıkışıklığı giderin.\n9. Kombiyi resetleyerek çalıştırın."
        ),
        FaultCode(
            brand = "DemirDöküm",
            type = "Kombi",
            code = "F02",
            title = "Kullanım Suyu Sensörü (NTC) Hatası",
            reason = "Sıcak su devresi sıcaklık algılama sensöründe kısa devre veya temassızlık mevcuttur. Cihaz kullanım suyu sıcaklığını algılayamaz, sıcak su vermez.",
            part = "Sıcak Su NTC Sensörü",
            installation = "1. Gaz ve elektrik bağlantılarını tamamen kapatın.\n2. Kombi altındaki vanalardan kullanım suyu girişini kapatıp, sıcak su musluğunu açarak kombideki suyu tamamen boşaltın.\n3. Ön yüzeyi açıp, pirinç boru üzerinde daldırma tip veya mandallı tip NTC sıcaklık sensörünü bulun.\n4. Soket bağlantısını çıkarın.\n5. Anahtar vasıtasıyla sensörü sökün. (Daldırma tip için sızdırmazlık teflon contası hazırlayın)\n6. Yeni NTC sensörün dişli kısmına teflon bant çekip yerine vidalayın ve hafifçe sıkın.\n7. Soketi geri takın.\n8. Suyu açıp kaçak kontrolü yapın ve kombiye elektrik verin."
        ),
        FaultCode(
            brand = "DemirDöküm",
            type = "Kombi",
            code = "F04",
            title = "İyonizasyon / Ateşleme Hatası",
            reason = "Kombi brülördeki alevi algılayamadığından ateşleme başarısız olmuştur. Tesisatta gaz yokluğu, gaz valfi bozukluğu veya iyonizasyon elektrot kirli olabilir.",
            part = "İyonizasyon Elektrotu, Ateşleme Trafosu, Gaz Valfi",
            installation = "1. Daireye gaz girişinin açık olduğundan (ocakta gaz kontrolü ile) emin olun.\n2. Kombi kapağını açıp yanma odası kapağını sökün.\n3. Brülörün yanındaki ince, ucu bükük demir elektrotu bulun.\n4. Elektrot kararmış veya oksitlenmişse, hassas bir ince zımpara yardımıyla temizleyin.\n5. Kablo bağlantı soketlerinin yerine oturduğunu teyit edin.\n6. Elektrot tamamen deforme olduysa vidasını gevşetip yenisiyle değiştirin.\n7. Eğer sorun çözülmezse gaz valfinin bobin sargılarını ölçün veya karttan gaz valfine voltaj geldiğini kontrol edin.\n8. Temizleme bittikten sonra yanma odası kapağını mutlaka sıkıca geri kapatıp vidalayın."
        ),
        FaultCode(
            brand = "DemirDöküm",
            type = "Kombi",
            code = "F05",
            title = "Gaz Geri Tepmesi / Baca Akış Prosestat Hatası",
            reason = "Kombi atık gaz tahliyesini gerçekleştirememektedir. Prosestat kontağı açmıyor, fan motoru çalışmıyor veya hermetik baca borusu yerinden çıkmış olabilir.",
            part = "Hava Prosestatı, Fan Motoru, Elektronik Kart",
            installation = "1. Elektrik şalterini kapatın.\n2. Kombinin üst kısmında yer alan fan motorunun dönüp dönmediğini elle kontrol edin (toz/çapak pompayı bloke etmiş olabilir).\n3. Fanın yanında iki adet silikon hortum bağlı yuvarlak plastik parçayı (Prosestat) bulun.\n4. Hortumları söküp fana üfleme kanalını ve hortumları temizleyin.\n5. Prosestatın uçlarını avometre ile sesli konuma alıp hortumdan hafifçe nefesinizle vakum yapın. Kontak klik sesi gelip devre kapanmalı. Kontak geçişi yoksa prosestatı yenileyin.\n6. Fan motorunun sargıları yanmışsa fanı söküp yenisini monte edin.\n7. Baca borusunun dışarıya eğiminin (-1 veya -2 derece aşağı yönlü) doğru olduğunu kontrol edin."
        ),
        FaultCode(
            brand = "DemirDöküm",
            type = "Kombi",
            code = "F10",
            title = "Tesisat Düşük Su Basınç Hatası",
            reason = "Kalorifer sistemi içerisindeki su basıncı güvenlik sınırı olan 0.8 bar seviyesinin altına düştüğünde ortaya çıkar. Cihazın kuru yanıp hasar görmemesi için sistemi emniyet amaçlı kilitler.",
            part = "Doldurma Musluğu, Su Basınç Sondası",
            installation = "1. Kombinin alt panelindeki siyah veya mavi renkli plastik doldurma musluğu vanasını bulun.\n2. Vanayı sola (saat yönünün tersine) yavaşça çevirerek sisteme su doldurmaya başlayın.\n3. Ön display panelinden bar basıncının 1.5 - 1.6 seviyesine gelmesini izleyin.\n4. İstenen değere gelince doldurma musluğunu saat yönünde iyice sıkın.\n5. Eğer ekranda basınç hala yükselmiyorsa, alt kapağı açıp su basınç sensörü (switch) soketlerini ve tıkanıklıkları kontrol edip gerekirse yenileyin."
        ),

        // === BAYMAK KOMBİ ===
        FaultCode(
            brand = "Baymak",
            type = "Kombi",
            code = "E01",
            title = "Ateşleme Başarısızlığı (Gaz Yok Hatası)",
            reason = "Cihaza gaz akışı gelmiyor ya da alev algılama elektrotu çalışmıyor. Kombi 3 ateşleme denemesinden sonra kendini emniyet kilidine alır.",
            part = "İyonizasyon ve Ateşleme Buji Seti, Gaz Armatürü",
            installation = "1. Kombinin altındaki gaz emniyet vanasının boruya paralel (açık) konumda olduğunu kontrol edin.\n2. Kombi panelinden 'Reset' butonuna 3 saniye basılı tutarak hata kilidini çözün.\n3. Eğer alev oluşup sönüyorsa, Brülör kapağını açıp iyonizasyon elektrotunu sökün, ince zımpara ile üstündeki isi silin ve tekrar takın.\n4. Gaz valfine elektrik gelmesine rağmen gaz çıkışı yoksa, gaz valfini (Honeywell veya SIT 845) 4 adet alyan vidasından çözüp yenisiyle değiştirin ve conta sabitlemesine dikkat edin."
        ),
        FaultCode(
            brand = "Baymak",
            type = "Kombi",
            code = "E03",
            title = "Hava Akış Anahtarı (Prosestat) Arızası",
            reason = "Hava akış kontrolünü sağlayan prosestat kapalı devre vermiyorsa kombi çalışmaz. Fan kirliliği veya baca tıkanıklığı da neden olur.",
            part = "Diferansiyel Basınç Sensörü (Prosestat), Venturi Borusu",
            installation = "1. Fan motorunun temiz canavarlarını kontrol edin, toz birikmişse fan kanatçıklarını diş fırçasıyla temizleyin.\n2. Fanın ucundaki dar boğazlı plastik parçayı (Venturi) söküp kireçlenmesini kontrol edin.\n3. Yeni prosestatı metal montaj plakasına sabitleyin.\n4. İki silikon borunun 'P1' ve 'P2' (Artı-Eksi) uçlarına doğru şekilde takıldığından emin olun. Ters takılırsa kombi hava akışı almıyor diye hata verir."
        ),
        FaultCode(
            brand = "Baymak",
            type = "Kombi",
            code = "E10",
            title = "Tesisat Düşük Su Basınç Hatası",
            reason = "Kalorifer devresindeki su miktarının yetersiz olması (basıncın 1.0 barın altında olması). Kombiyi susuz çalıştırmamak için emniyet sistemidir.",
            part = "Su Basınç Switchi / Doldurma Musluğu",
            installation = "1. Kombinin altındaki genellikle mavi veya siyah renkli plastik doldurma musluğunu saat yönünün tersine yavaşça çevirin.\n2. Kombinin ön ekranındaki bar göstergesini takip edin, basınç 1.5 bar değerine ulaştığında vanayı sıkıca saat yönünde kapatın.\n3. Basınç göstergesi hala yükselmiyorsa, basınç switchi ucunu söküp kireç tıkamış mı bakın, tıkanmışsa iğneyle açın veya switchi değiştirin."
        ),
        FaultCode(
            brand = "Baymak",
            type = "Kombi",
            code = "E02",
            title = "Aşırı Isınma Emniyet Kilitlemesi",
            reason = "Kombi içindeki su sıcaklığının 105°C limit değerini geçmesinden dolayı emniyet termostatı devreye girmiştir. Pompa blokajı veya tesisat havası etkilidir.",
            part = "Aşırı Isınma Limit Termostatı, Pompa",
            installation = "1. Elektrik şalterini kapatıp kombinin soğumasını bekleyin.\n2. Kalorifer tesisat borularındaki havayı almak için radyatör pürjör vidalarından hava alma anahtarı ile havasını tamamen boşaltın.\n3. Limit termostatın üzerindeki küçük reset düğmesine basıp resetleyin.\n4. Pompa motorunu söküp iç milinin rahat döndüğünü kontrol edin, aşınmış pompayı sıfırı ile değiştirin."
        ),

        // === BOSCH KOMBİ ===
        FaultCode(
            brand = "Bosch",
            type = "Kombi",
            code = "A7",
            title = "Sıcak Su Kullanım NTC Arızası",
            reason = "Sıcak kullanım suyu sıcaklık sensöründe kesinti veya elektriksel kısa devre tespit edildi. Cihaz stabil sıcak su veremez.",
            part = "Kullanım Suyu Isı Sensörü",
            installation = "1. Elektrik şalterini indirip kombinin altındaki sıcak su giriş valfini kapatın.\n2. Kombi muhafaza kapağını açıp sıcak su çıkış borusu üzerindeki klipsli pirinç sensörü sökün.\n3. Yeni NTC sıcaklık sensör flanşını boruya mandal klipsi ile sabitleyin.\n4. Soket bağlantısını sıkıca tam oturtup sızdırmazlık testi yapın."
        ),
        FaultCode(
            brand = "Bosch",
            type = "Kombi",
            code = "C6",
            title = "Diferansiyel Basınç Şalteri Kapanmıyor",
            reason = "Gaz atma fanı devreye girdiğinde, diferansiyel basınç şalteri (prosestat) kontakları doğru basınç basıncı olmadığı için kapanmamıştır.",
            part = "Hava Diferansiyel Basınç Şalteri, Fan",
            installation = "1. Ön kapağı açıp duman çıkışındaki fan motoru salyangozunu kontrol edin.\n2. Prosestata bağlı olan silikon ölçüm tüplerini sökün, içinde nem birikmişse hava üfleyerek kurutun.\n3. Yeni basınç şalterini 0.45 mbar milibar tırnak değerine uygun olacak şekilde yerine takıp soketleri yerleştirin."
        ),
        FaultCode(
            brand = "Bosch",
            type = "Kombi",
            code = "EA",
            title = "Alev Algılanmadı / Ateşleme Kesintisi",
            reason = "Kombi alevi denemesine rağmen alev oluşmadı veya iyonizasyon akımı yeterli seviyede karta ulaşmadı. Gaz valfi açmıyor olabilir.",
            part = "Gaz Valfi, İyonizasyon Elektrotu, Kart",
            installation = "1. Dairenin gaz giriş vanasının dikey (açık) olduğunu teyit edin.\n2. Yanma haznesindeki iyonizasyon elektrot ucunu temiz bezle sıyırıp ince kararmaları giderin.\n3. Gaz armatürünün bobin direncini ölçün (24V DC besleme voltajı gelmeli), değer okunamıyorsa gaz valfini sıfır SIT ile yenileyin."
        ),

        // === VAILLANT KOMBİ ===
        FaultCode(
            brand = "Vaillant",
            type = "Kombi",
            code = "F22",
            title = "Kuru Yanma / Yetersiz Su Basıncı",
            reason = "Kombi ısı blok borularında hiç su kalmamıştır veya basınç 0.6 barın altındadır. Sistem pompayı çalıştırıp aşırı hararet etmemek için kilitlenir.",
            part = "Su Basınç Switchi, Pompa Devri",
            installation = "1. Kombi altındaki siyah doldurma vanasını hafifçe açıp su basıncını 1.5 bar değerine getirin.\n2. Eğer su doluyor ama hala F22 yanıyorsa su basınç sensörü (pirinç gövdeli kablolu) tırnağı paslanmıştır.\n3. Basınç sensörünü soketinden ve cıvatasından sökerek yerine yenisini vidalayın ve segman kilidini geçirin."
        ),
        FaultCode(
            brand = "Vaillant",
            type = "Kombi",
            code = "F28",
            title = "Başarısız Ateşleme Arızası",
            reason = "Cihaza gaz girişi yapılamıyor, gaz valfi açmıyor, ateşleme elektrotu kıvılcım çakmıyor veya elektronik anakart ateşleme trafosu arızalı.",
            part = "Ateşleme Elektrotu, Gaz Grubu Valfi, Anakart",
            installation = "1. Şebeke ve kombi giriş gaz vanasını kontrol edip gaz olduğunu sınayın.\n2. Ateşleme elektrotunun ucunu brülöre uzaklığını (2-3 mm olmalı) ayarlayın.\n3. Voltaj kontrolü yapıp gaz valfinin açıldığından emin olun, açılmıyorsa valfi değiştirin."
        ),

        // === BUDERUS KOMBİ ===
        FaultCode(
            brand = "Buderus",
            type = "Kombi",
            code = "3C",
            title = "Diferansiyel Basınç Şalteri Devre Dışı",
            reason = "Fan çalışmasına rağmen baca akış prosestatı kontak vermemiştir, fan kanatları pislikten tıkanmış veya bacada kuş girmesi gibi tıkanma mevcut.",
            part = "Prosestat, Fan Pervanesi, Baca Borusu",
            installation = "1. Hermetik baca borusunu dışarıya doğru meyilini kontrol edip tıkanıklık varsa temizleyin.\n2. Fan motorunu söküp pervane kanatlarındaki kurumları temizleyin.\n3. Prosestatı yeni parça kodu ile değiştirip silikon borularını bağlayın."
        ),
        FaultCode(
            brand = "Buderus",
            type = "Kombi",
            code = "4C",
            title = "Emniyet Limit Termostat Arızası",
            reason = "Kalorifer gidiş hattındaki su sıcaklığı 98°C'yi aşmıştır. Pompa hava yapmış veya kalorifer çamur filtresi tıkanmış olabilir.",
            part = "Limit Termostat, Kalorifer Filtresi, Pompa",
            installation = "1. Kombiyi şalterden kapatın.\n2. Kombinin dönüş borusu altındaki su filtresini (metal süzgeç) söküp kireç ve çamur tortularını fırçalayarak temizleyin.\n3. Limit termostatın üstündeki küçük pimi bastırıp yerine oturtun ve kombiyi çalıştırın."
        ),

        // === BEKO & ARÇELİK BUZDOLABI ===
        FaultCode(
            brand = "Beko",
            type = "Buzdolabı",
            code = "E0",
            title = "Dondurucu Sensör Arızası",
            reason = "Buzdolabının dondurucu (Freezer) bölümünde ısının doğru okunmasını sağlayan sensör değer kaybetmiş ya da açık devre oluşturmuştur.",
            part = "Dondurucu Evaporatör NTC Sensörü",
            installation = "1. Buzdolabının fişini çekip elektrik bağlantısını sonlandırın.\n2. Dondurucu kısmının içindeki tüm çekmeceleri ve arka plastik koruyucu paneli vidalarından sökerek çıkarın.\n3. Evaporatör (alüminyum petek) borularına plastik kelepçeyle sabitlenmiş, ucunda damla şeklinde beyaz plastik olan NTC sensörünü bulun.\n4. Sensör soketini yuvasından çıkarın.\n5. Yeni sensörü aynı boru bölgesine plastik kelepçe yardımıyla sıkıca bağlayın (gevşek kalırsa sıcaklığı yanlış ölçer ve karlanma yaptırır).\n6. Soketini düzgünce kilitli yerine takıp arka kapağı kapatın.\n7. Buzdolabını en az 2 saat dinlendirip çalıştırın."
        ),
        FaultCode(
            brand = "Beko",
            type = "Buzdolabı",
            code = "E4",
            title = "Defrost Isıtıcı Çalışmıyor (Buzlanma Hatası)",
            reason = "Buzdolabında otomatik kar eritme işleminde görevli rezistansın yanması sonucu kar birikmiştir. Fan çalışmasına rağmen soğuk hava kanallardan geçemez, dolap soğutmaz.",
            part = "Defrost Rezistansı, Termal Sigorta",
            installation = "1. Cihazın elektriğini kesin.\n2. Dondurucu arka panelini sökerek evaporatörü ortaya çıkarın.\n3. Eğer evaporatör tamamen kardan bir kalıp halindeyse saç kurutma makinesiyle eriterek temizleyin.\n4. Evaporatörün altına ve yanlarına sarılmış olan metal cam tüplü veya metal borulu defrost rezistansını bulun.\n5. Avometre ile rezistans uçlarındaki omajı ölçün, sonsuz değer (açık devre) varsa rezistans kopmuştur.\n6. Rezistansı klipslerinden dikkatlice çıkarın (alüminyum petek kanatçıklarına zarar vermemeye dikkat edin).\n7. Yeni rezistansı ve bağlı olan emniyet termal sigortasını ilgili yuvalara geçirip kablo soketlerini sızdırmaz şekilde birleştirin.\n8. Paneli kapatmadan önce su tahliye deliğinin açık olduğunu kontrol etmek için su dökün."
        ),
        FaultCode(
            brand = "Beko",
            type = "Buzdolabı",
            code = "E8",
            title = "Kondanser Fan Motoru Arızası",
            reason = "Kompresörün yanındaki kondanser fanının durması sonucu gaz soğutulamamakta ve kompresör aşırı ısınıp termik açmaktadır.",
            part = "Kondanser Fan Motoru",
            installation = "1. Dolabın arkasındaki alt koruma sacını söküp motor dairesini açın.\n2. Fan pervanesine kablo veya pislik sıkışmış mı elinizle kontrol edin.\n3. Fan motoru kablolarını söküp yerine yeni fan motorunu somun ve vidaları ile monte edin, soketlerini bağlayın."
        ),
        FaultCode(
            brand = "Arçelik",
            type = "Buzdolabı",
            code = "E1",
            title = "Soğutucu Bölüm Evaporatör Sensör Hatası",
            reason = "Sebzelik ve taze gıda bölümündeki gizli veya açık evaporatör sensöründe arıza. Dolapta alt tarafta aşırı soğutma, terleme veya karlanma görülür.",
            part = "Soğutucu NTC Sensörü",
            installation = "1. Dolabı fişten çekip panelleri açın.\n2. Soğutucu bölmen içindeki sıcaklık algılayıcı NTC'yi bulun (genellikle yan duvarda küçük bir plastik ızgara arkasındadır).\n3. Izgarayı ince uçlu tornavidayla kastırıp çıkarın.\n4. Kabloyu arkadaki ek yerinden kesin (Beko/Arçelik yedek parça sensörleri genellikle hazır makaron kitiyle gelir).\n5. Yeni sensör kablosunu eski kablolara lehimleyin veya ek sızdırmaz klemensi kullanarak birleştirin.\n6. Ek yerine fön makinesiyle ısıyla daralan makaronu büzüştürerek nem almasını önleyin ve kapağı kapatın."
        ),

        // === BEKO & ARÇELİK FIRIN ===
        FaultCode(
            brand = "Beko",
            type = "Fırın",
            code = "F1",
            title = "Lokal Aşırı Isınma Arızası",
            reason = "Fırın içi derecesi ayarlanan değerin çok üzerine çıkmış veya limit emniyet termostatı atmıştır. Genellikle soğutma fanı çalışmadığında veya anakart rölesi yapışıp kaldığında oluşur.",
            part = "Emniyet Limit Termostatı, Fırın Soğutma Fanı",
            installation = "1. Fırının fişini çekin veya sigortasını indirin.\n2. Fırını dolaptan dışarı çekip üst ve arka sac kapaklarını sökün.\n3. Fırın tavan gövdesine basan, üstünde küçük kırmızı/siyah bir pim olan yuvarlak emniyet termostatını bulun.\n4. Eğer pimi atmışsa üstüne bastırarak resetleyin (klik sesi gelir).\n5. Termostat bozulmuşsa 2 adet vidasından sökerek yenisini sac gövdeye tam oturacak şekilde vidalayın.\n6. Fırın arkasındaki teğetsel soğutma fanının pervanesinin rahat döndüğünü kontrol edin, sıkışmışsa yağlayın veya motorunu değiştirin."
        ),
        FaultCode(
            brand = "Arçelik",
            type = "Fırın",
            code = "F3",
            title = "Isı Sensörü (PT1000) Hatası",
            reason = "Elektronik kontrol kartı ısı değerini okuyan direnç sensörüyle bağlantısını kaybetmiştir. Ekranda fırın çalıştırılamaz hatası görünür.",
            part = "PT1000 Isı Sensör Probu",
            installation = "1. Cihaz elektrik gücünü devreden çıkarın.\n2. Fırın kapağını açıp fırın hücresi içindeki arka köşede duran ince metal çubuk şeklindeki sensörü bulun.\n3. Fırının arkasındaki koruyucu sacı sökün.\n4. Isı sensörünün fırın gövdesine arkadan giren vidalarını çözüp probu dışarı çekin.\n5. Kablo soketini prizinden ayırın.\n6. Yeni PT1000 sensör probunu yuvaya sokup vidalayın, kablosunu sokete bağlayıp arka sacı geri kapatın."
        ),

        // === TELEVİZYONLAR (BEKO/ARÇELİK/VESTEL) ===
        FaultCode(
            brand = "Beko",
            type = "Televizyon",
            code = "LED3",
            title = "Kırmızı LED 3 Kez Yanıp Sönüyor (Power Board Hatası)",
            reason = "TV besleme kartındaki standby (kondansatör gerilimleri) veya ana hat voltaj değerlerinde dalgalanma veya kısa devre saptanmıştır.",
            part = "Besleme (Power Supply) Kartı, Şişmiş Kondansatör Grubu",
            installation = "1. TV'yi düz yumuşak bir zemine (ekranı alta gelecek şekilde köpük üstüne) yatırın.\n2. Arka kapak üzerindeki tüm vidaları sökün.\n3. Elektrik kablosunun girdiği sarı/yeşil renkli büyük PCB kartı olan Besleme Kartını görün.\n4. Kart üzerindeki silindirik kondansatörlerin üst kısımlarını inceleyin. Eğer üst kısımları yukarı doğru şişmiş veya sızıntı yapmışsa arızalıdır.\n5. Kondansatörleri havyayla söküp aynı mikrofarat (uF) ve voltaj (V) değerinde yenileriyle lehimleyebilirsiniz.\n6. Kart tamamen yanmışsa tüm soketleri tırnaklarından çekip sökün, kartı vidalarından çıkarıp yeni orijinal besleme kartını yerleştirip vidalayın "
        ),
        FaultCode(
            brand = "Arçelik",
            type = "Televizyon",
            code = "SYG",
            title = "Ses Var Görüntü Yok (Backlight LED Arızası)",
            reason = "Televizyon açılıyor, ses geliyor, kumanda çalışıyor ancak panele ışık veren LED barların biri patladığı için ekran karanlık kalır (fener tutulduğunda gölge görüntüler saptanabilir).",
            part = "LED Backlight Şerit Seti",
            installation = "1. Bu işlem son derece hassastır, panel camını kırmamaya özen gösterin!\n2. TV arka kapağını ve çerçeve vidalarını açın.\n3. Ön çerçeve tırnaklarını kurtarıp LCD panel camını vantuzlar yardımıyla çok dikkatlice kaldırıp güvenli bir köşeye koyun.\n4. Beyaz reflektör kağıdını çıkarın ve altındaki metal tabana monte edilmiş LED şeritleri bulun.\n5. LED test cihazı ile hangi şeridin patlak olduğunu ölçün.\n6. Çift taraflı bantla yapıştırılmış eski LED şeritleri sökün.\n7. Yeni orijinal LED şeritleri yapışkanlarını sıyırarak yuvaya yapıştırın ve soket bağlantılarını takın.\n8. Enerji verip tüm LED'lerin yandığını görün. Sonra LCD cam ve katmanlarını dikkatlice sırasıyla geri toplayın."
        ),
        FaultCode(
            brand = "Vestel",
            type = "Televizyon",
            code = "V01",
            title = "Standby Voltaj Çökmesi (Kırmızı Işık Sürekli Yanıp Sönüyor)",
            reason = "Besleme (Vestel IPS11-IPS20 serisi) kartında yer alan Schottky diyotlarının kısa devreye düşmesiyle ana kart koruma modunun kilitlenmesidir.",
            part = "Schottky Diyotları (besleme kartı)",
            installation = "1. TV emniyetli şekilde arkasını çevirip kapağını sökün.\n2. Besleme kartını vidalarından çıkarın.\n3. Isı sönümleyici metal blok etrafında yer alan kalın zımba bacaklı diyotları (UF5402 vb.) kontrol edin.\n4. Avometre ile kısa devre bip testi yapın. Kısa devre yapan bozuk diyotu sökün.\n5. Yeni diyotu aynı yön çizgisine (katot kutubu) uygun şekilde lehimleyin ve test edin."
        ),

        // === PELET SOBASI / KOMBİSİ ===
        FaultCode(
            brand = "DemirDöküm",
            type = "Pelet",
            code = "E101",
            title = "Ateşleme Buharı / Rezistans Hatası",
            reason = "Pelet sobasında yakıtın tutuşmasını sağlayan seramik veya metal elektrikli buji rezistansı ısınmıyor ya da pelet doldurma helezonu tıkalı.",
            part = "Seramik Pelet Ateşleme Bujisi (Rezistans), Helezon Motoru",
            installation = "1. Sobayı tamamen kapatıp fişini çekin. Cihazın soğuduğundan emin olun.\n2. Yan ve arka sac panelleri vidalarından gevşeterek çıkarın.\n3. Sobanın yanma potasının hemen altına/arkasına uzanan kalın fişek şeklindeki kablolu bujiyi bulun.\n4. Bujiyi tutan sabitleme cıvatasını gevşetin ve arkasından çekerek çıkarın.\n5. Kablo klemenslerini anakart veya besleme klemensinden sökün.\n6. Yeni seramik bujiyi yuvaya tam oturacak şekilde yerleştirip vidasını sıkın. Seramik uçların kırılmamasına dikkat edin.\n7. Kablo bağlantılarını yapıp cihazı ilk çalıştırma moduna alarak kontrol edin."
        ),
        FaultCode(
            brand = "Baymak",
            type = "Pelet",
            code = "E108",
            title = "Egzoz Gazı Fan / Baca Basınç Hatası",
            reason = "Pelet yanma esnasında oluşan egzoz dumanının dışarı tahliyesinde sorun var. Baca boruları pelet külüyle tıkanmış veya duman fanı sıkışmış olabilir.",
            part = "Egzoz Duman Fan Motoru, Diferansiyel Hava Manometresi",
            installation = "1. Sobanın arkasındaki t-baca temizleme kapağını açın ve içindeki pelet külü birikintisini elektrik süpürgesiyle vakumlayarak çekin.\n2. Soba içindeki egzoz fan motorunun kablosunu ve soketini çıkarın.\n3. Fan motoru gövdesini tutan 4-5 adet vidayı dairesel olarak sökün ve fanı çıkarın.\n4. Kanatlardaki kurumu tel fırça ve tiner yardımıyla temizleyin veya motor sargısı bozuksa yeni motoru salmastra contasıyla birlikte vidalayın. Conta hava kaçırmamalıdır.\n5. Basınç hortumunun ucunu vakum kanallarından temizleyin ve her şeyi geri toplayın."
        ),

        // === ÇAMAŞIR MAKİNELERİ ===
        FaultCode(
            brand = "Arçelik",
            type = "Çamaşır Makinesi",
            code = "E01",
            title = "Kapı Kilidi Algılanmadı",
            reason = "Çamaşır makinesi kapağının tam kapanmaması, kapı emniyet kilidindeki tırnağın yerleşmemesi veya kilidin bobininin yanmış olması.",
            part = "Kapı Emniyet Kilidi Switchi",
            installation = "1. Makinenin fişini çekin.\n2. Kapak körük lastiğinin etrafındaki metal gergi telini düz tornavidayla kastırıp çıkarın ve lastiğin sağ kısmını gövdeden ayırın.\n3. Kilidin önündeki iki vidayı sökün.\n4. Elinizi gövde ile lastik arasından içeri sokarak kapı kilidini arkadan tutup öne çıkarın.\n5. Kablo soketini söküp yeni emniyet kilidine bağlayın.\n6. Kilidi gövdeye vidalayıp körük lastiğini yerine oturtun ve gergi yaylı telini geri takın."
        ),
        FaultCode(
            brand = "Arçelik",
            type = "Çamaşır Makinesi",
            code = "E08",
            title = "Motor Triyak Kısa Devre Dönmeme Hatası",
            reason = "Kazan döndürücü kolektör motorunun aşırı yüklenme veya triyak sızıntısı sonucu dönmesinin bloke olması veya kömürlerin bitmesi.",
            part = "Motor Kömür Fırçası, Anakart Motor Triyağı",
            installation = "1. Makineyi arkasını açıp tahrik kayışını sökün.\n2. Motoru gövdeye bağlayan 2 adet alyan cıvatayı sökün ve soketini karttan çıkartarak motoru alt şasiden dışarı alın.\n3. Motor arkasında bulunan siyah plastik tutuculu 2 adet kömür fırçayı bulun.\n4. Kömürlerin yay boyunu ölçün. 1 cm'den kısaysa kömürleri yenileyip klemenslerini geri oturtun ve motor aksını elinizle çevirip sesini dinleyin."
        ),

        // === BULAŞIK MAKİNELERİ ===
        FaultCode(
            brand = "Beko",
            type = "Bulaşık Makinesi",
            code = "E02",
            title = "Su Giriş / Ventili Hatası (Su Almıyor)",
            reason = "Bulaşık makinesi belirlenen sürede su alamamaktadır. Evde sular kesik, musluk kapalı veya cihaz girişindeki su giriş ventili arızalı olabilir.",
            part = "Su Giriş Ventili (Akış Sensörlü)",
            installation = "1. Musluğu kapatın ve su giriş hortumunu cihazdan sökün.\n2. Makinenin arka sac kapağını veya alt servis kapağını sökün.\n3. Giriş hortumunun bağlandığı pirinç/plastik dişli bobinli vana olan Ventili bulun.\n4. Kablo fişlerini sökün, su hortumu kelepçesini penseyle gevşetip hortumu ventilden çekin.\n5. Yeni ventili yerine vidalayıp iç hortumu takıp kelepçe ile sıkın, elektrik soketlerini takıp sızıntı kontrolü yapın."
        )
    )
}
