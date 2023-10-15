enum class Nivel { BASICO, INTERMEDIARIO, DIFICIL }

class Usuario(val nome: String, val email: String) {
    val formacoesInscritas = mutableListOf<Formacao>()

    fun matricularNaFormacao(formacao: Formacao) {
        if (formacao !in formacoesInscritas) {
            formacoesInscritas.add(formacao)
            formacao.matricular(this)
        }
    }

    fun visualizarProgresso(formacao: Formacao): Double {
        val totalAulas = formacao.conteudos.size
        if (totalAulas == 0) {
            return 0.0 
        }
        val aulasConcluidas = formacao.aulasConcluidas[this] ?: 0
        return (aulasConcluidas.toDouble() / totalAulas) * 100
    }

    fun marcarAulaConcluida(formacao: Formacao, conteudo: ConteudoEducacional) {
        formacao.marcarAulaConcluida(this, conteudo)
    }
}

data class ConteudoEducacional(var nome: String, val duracao: Int = 60, val nivel: Nivel)

class Formacao(val nome: String, val nivel: Nivel) {
    val conteudos = mutableListOf<ConteudoEducacional>()
    val inscritos = mutableListOf<Usuario>()
    val aulasConcluidas = mutableMapOf<Usuario, Int>()

    fun matricular(usuario: Usuario) {
        inscritos.add(usuario)
        aulasConcluidas[usuario] = 0
        usuario.formacoesInscritas.add(this)
    }

    fun listarInscritos(): List<Usuario> {
        return inscritos
    }

    fun adicionarConteudo(conteudo: ConteudoEducacional) {
        conteudos.add(conteudo)
    }

    fun marcarAulaConcluida(usuario: Usuario, conteudo: ConteudoEducacional) {
        if (conteudo in conteudos) {
            aulasConcluidas[usuario] = (aulasConcluidas[usuario] ?: 0) + 1
        }
    }
}

fun main() {
    val conteudo1 = ConteudoEducacional("Introdução ao Kotlin", 90, Nivel.BASICO)
    val conteudo2 = ConteudoEducacional("Classes e Objetos em Kotlin", nivel = Nivel.INTERMEDIARIO)
    val conteudo3 = ConteudoEducacional("Desenvolvimento mobile em Kotlin", nivel = Nivel.INTERMEDIARIO)

    val formacaoKotlin = Formacao("Formação Kotlin", Nivel.INTERMEDIARIO)

    val usuario1 = Usuario("JoãoEnterprises", "jao@email.com")
    val usuario2 = Usuario("Leticie", "letixeee@email.com")

    formacaoKotlin.matricular(usuario1)
    formacaoKotlin.matricular(usuario2)

    usuario1.matricularNaFormacao(formacaoKotlin)

    usuario1.marcarAulaConcluida(formacaoKotlin, conteudo1)
    usuario1.marcarAulaConcluida(formacaoKotlin, conteudo2)

    println("Usuários inscritos na formação Kotlin: ${formacaoKotlin.listarInscritos().map { it.nome }}")
    
    val progressoAlice = usuario1.visualizarProgresso(formacaoKotlin)
    if (progressoAlice.isNaN()) {
        println("Progresso de JoãoEnterprises na formação Kotlin: 0.0%")
    } else {
        println("Progresso de JoãoEnterprises na formação Kotlin: ${progressoAlice}%")
    }

    formacaoKotlin.adicionarConteudo(conteudo3)

    println("Conteúdos de nível INTERMEDIARIO na formação Kotlin: ${formacaoKotlin.conteudos.filter { it.nivel == Nivel.INTERMEDIARIO }.map { it.nome }}")
}





