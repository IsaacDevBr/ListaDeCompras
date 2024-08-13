package livrokotlin.com.br


import android.content.pm.PackageManager
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import livrokotlin.com.br.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val PERMISSION_REQUEST_CODE = 1001
    private var selectedBitmap: Bitmap? = null // Variável para armazenar o Bitmap selecionado

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Converte a imagem para bitmap
                selectedBitmap = uriToBitmap(it) // Armazena o Bitmap na variável
                binding.imgFotoProduto.setImageBitmap(selectedBitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //clincando na imagem para inserir uma foto
        binding.imgFotoProduto.setOnClickListener {

            // Verifica se a permissão já foi concedida
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Se a permissão não foi concedida, solicita a permissão
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                abrirGaleria()
            } else {
                Toast.makeText(
                    this,
                    "Permissão para acessar a galeria necessária",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //definição do ouvinte do botão
        binding.btnInserir.setOnClickListener {

            //pegando o valor digitado pelo usuario
            val produto = binding.txtProduto.text.toString()
            val qtd = binding.txtQtd.text.toString()
            val valor = binding.txtValor.text.toString()


            if (produto.isNotEmpty() && qtd.isNotEmpty() && valor.isNotEmpty()) {
                //criando um objeto com as informações que o usuario inseriu
                val prod = Produto(produto, qtd.toInt(), valor.toDouble(), selectedBitmap)
                Utils.produtosGlobal.add(prod)

                binding.txtProduto.text.clear()
                binding.txtQtd.text.clear()
                binding.txtValor.text.clear()
                selectedBitmap = null
            } else {
                binding.txtProduto.error =
                    if (binding.txtProduto.text.isEmpty()) "Preencha o nome do produto"
                    else null

                binding.txtQtd.error = if (binding.txtQtd.text.isEmpty()) "Preencha a quantidade"
                else null

                binding.txtValor.error = if (binding.txtValor.text.isEmpty()) "Preencha o valor"
                else null
            }

        }
    }

    //função para abrir a galeria
    private fun abrirGaleria() {
        getContent.launch("image/*")
    }

    //função para converter uri em bitmap
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
