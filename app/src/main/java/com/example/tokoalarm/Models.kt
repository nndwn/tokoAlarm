package com.example.tokoalarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
)

data class RegisterResponse(
    val status: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData (
    val id: String,
    val nama: String,
    val nohp: String,
    val password: String
)

data class DataPelangganResponse(
    val status :Boolean,
    val saldo :String,
    val data :List<ListPromo>,
    val config: Config
)

//Todo: Link Tutorial tidak sesuai pada API diberikan
data class Config(
    @SerializedName("link_tutorial")
    val linkTutorial : String,
    @SerializedName("link_pesan_alarm")
    val linkPesanAlarm :String
)
//Todo: Pada list Promo tidak terdapat link di API
data class ListPromo(
    @SerializedName("updated_at")
    val updatedAt :String,
    val banner :String,
    @SerializedName("created_at")
    val createdAt :String,
    val id :String,
    val deskripsi :String,
    val title :String
)

data class TopUpResponse(
    val status: Boolean,
    val data: List<ListTopUpData>
)

data class ListTopUpData(
    val keterangan :String,
    val noreff :String,
    val jumlah :String,
    @SerializedName("updated_at")
    val updatedAt :String,
    @SerializedName("id_users")
    val idUsers :String,
    @SerializedName("created_at")
    val cratedAt :String,
    val id :String,
    @SerializedName("saldo_awal")
    val saldoAwal :String,
    val tipe :String,
    @SerializedName("saldo_akhir")
    val saldoAkhir :String,
    val status :String
)


data class BankAccount(
    val id : Int,
    val name : String,
    val numberRek : String ,
    val owner :String
)

data class Price(
    val id : Int,
    val price: Int
)

data class Tone(
    val name : String,
    val value : Int
)

data class PaketResponse(
    val status: Boolean,
    val data : List<ListPaket>
)

data class ListPaket(
    val biaya : String,
    @SerializedName("updated_at")
    val updateAt : String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("biaya_rupiah")
    val biayaRupiah : String,
    val id :String,
    @SerializedName("cutoff_day")
    val cutoffDay : String,
    val periode : String,
    @SerializedName("day_convertion")
    val dayConvertion : String
)

data class ListAlatResponse(
    val status : Boolean,
    val data : List<ListAlat>
)

data class ListAlat(
    @SerializedName("nama_paket")
    var namePaket : String,
    @SerializedName("nomor_paket")
    val nomorPaket: String,
    @SerializedName("created_at")
    val createdAt : String,
    @SerializedName("biaya_rupiah")
    val biayaRupiah : String,
    @SerializedName("cutoff_day")
    val cutoffDay: String,
    val periode : String,
    @SerializedName("tanggal_selesai")
    val tanggalSelesai : String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val biaya : String,
    @SerializedName("tanggal_mulai")
    val tanggalMulai : String,
    @SerializedName("id_alat")
    var idAlat : String,
    val nohp : String,
    val id : String,
    @SerializedName("sisa_hari")
    val sisaHari : String,
    val status : String,
    @SerializedName("day_convertion")
    val dayConvertion : String
)

class SharedViewMainActivity : ViewModel() {
    val saldo : MutableLiveData<String> = MutableLiveData()
    val linkPemesanan : MutableLiveData<String> = MutableLiveData()
    val listAlat : MutableLiveData<List<ListAlat>> = MutableLiveData()
}

class SharedViewTopUp : ViewModel() {
    val price : MutableLiveData<Int> = MutableLiveData()
    val methodPayment : MutableLiveData<BankAccount> = MutableLiveData()
}

class SharedViewPilihPaket : ViewModel() {
    val paket : MutableLiveData<List<ListPaket>> = MutableLiveData()
    val position : MutableLiveData<Int> = MutableLiveData()
}

