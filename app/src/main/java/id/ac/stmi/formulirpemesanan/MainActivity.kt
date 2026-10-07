package id.ac.stmi.formulirpemesanan

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SimpleAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val daftarPesanan = mutableListOf<Map<String, String>>()
    private lateinit var rekapAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNama = findViewById<EditText>(R.id.etNama)
        val spVarian = findViewById<Spinner>(R.id.spVarian)
        val rgUkuran = findViewById<RadioGroup>(R.id.rgUkuran)
        val cbEspresso = findViewById<CheckBox>(R.id.cbEspresso)
        val cbBoba = findViewById<CheckBox>(R.id.cbBoba)
        val btnProses = findViewById<Button>(R.id.btnProses)
        val lvRekap = findViewById<ListView>(R.id.lvRekap)

        // Spinner: pilihan varian minuman
        val varianAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.varian_minuman,
            android.R.layout.simple_spinner_item
        )
        varianAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spVarian.adapter = varianAdapter

        // ListView: rekap pesanan (judul + sub judul)
        rekapAdapter = SimpleAdapter(
            this,
            daftarPesanan,
            android.R.layout.simple_list_item_2,
            arrayOf(KEY_JUDUL, KEY_DETAIL),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        lvRekap.adapter = rekapAdapter

        btnProses.setOnClickListener {
            val nama = etNama.text.toString().trim()
            if (nama.isEmpty()) {
                etNama.error = getString(R.string.error_nama_kosong)
                etNama.requestFocus()
                return@setOnClickListener
            }

            val varian = spVarian.selectedItem.toString()
            val ukuran = findViewById<RadioButton>(rgUkuran.checkedRadioButtonId).text.toString()

            val topping = mutableListOf<String>()
            if (cbEspresso.isChecked) topping.add(cbEspresso.text.toString())
            if (cbBoba.isChecked) topping.add(cbBoba.text.toString())
            val teksTopping = if (topping.isEmpty()) {
                getString(R.string.tanpa_topping)
            } else {
                topping.joinToString(", ")
            }

            daftarPesanan.add(
                mapOf(
                    KEY_JUDUL to "${daftarPesanan.size + 1}. $nama - $varian",
                    KEY_DETAIL to "Ukuran: $ukuran | Topping: $teksTopping"
                )
            )
            rekapAdapter.notifyDataSetChanged()
            lvRekap.smoothScrollToPosition(daftarPesanan.size - 1)

            // Reset form untuk pesanan berikutnya
            etNama.text.clear()
            spVarian.setSelection(0)
            rgUkuran.check(R.id.rbSmall)
            cbEspresso.isChecked = false
            cbBoba.isChecked = false

            Toast.makeText(this, R.string.pesanan_ditambahkan, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val KEY_JUDUL = "judul"
        private const val KEY_DETAIL = "detail"
    }
}
