# -*- coding: utf-8 -*-
"""Gera o SQL idempotente das perguntas complementares. Nao faz parte da API."""
from pathlib import Path

P = []

def q(enunciado, testamento, nivel, referencia, categoria, explicacao, alternativas):
    corretas = [item for item in alternativas if item[1]]
    if len(alternativas) != 4 or len(corretas) != 1:
        raise SystemExit(f"Alternativas invalidas: {enunciado}")
    P.append((enunciado, testamento, nivel, referencia, categoria, explicacao, alternativas))

def add_lote():
    q("Quem matou Abel?", "ANTIGO", "FACIL", "Gênesis 4:8", "Gênesis",
      "Caim se levantou contra Abel e o matou.",
      [("Caim", True), ("Sete", False), ("Noé", False), ("Lameque", False)])
    q("Qual filho Sara teve na velhice?", "ANTIGO", "FACIL", "Gênesis 21:3", "Gênesis",
      "Sara deu à luz Isaque.",
      [("Ismael", False), ("Isaque", True), ("Esaú", False), ("José", False)])
    q("Quem foi colocado num cesto à beira do rio?", "ANTIGO", "FACIL", "Êxodo 2:3", "Êxodo",
      "A mãe de Moisés o pôs num cesto no Nilo.",
      [("Arão", False), ("Josué", False), ("Moisés", True), ("Samuel", False)])
    q("Quem era a irmã de Moisés e Arão?", "ANTIGO", "FACIL", "Números 26:59", "Mulheres da Bíblia",
      "O texto nomeia Miriã como irmã de Arão e Moisés.",
      [("Débora", False), ("Miriã", True), ("Rute", False), ("Ana", False)])
    q("Quem era o pai de Davi?", "ANTIGO", "FACIL", "1 Samuel 16:11", "Personagens bíblicos",
      "Jessé apresentou Davi a Samuel.",
      [("Jessé", True), ("Saul", False), ("Boaz", False), ("Obede", False)])
    q("Quem era o grande amigo de Davi e filho de Saul?", "ANTIGO", "FACIL", "1 Samuel 18:3", "Personagens bíblicos",
      "Jônatas fez aliança com Davi.",
      [("Abner", False), ("Absalão", False), ("Jônatas", True), ("Joabe", False)])
    q("Quem reinou em Israel depois de Davi?", "ANTIGO", "FACIL", "1 Reis 2:12", "Reis de Israel",
      "Salomão sentou-se no trono de Davi.",
      [("Saul", False), ("Jeroboão", False), ("Roboão", False), ("Salomão", True)])
    q("Por quantos dias e noites choveu no dilúvio?", "ANTIGO", "FACIL", "Gênesis 7:12", "Gênesis",
      "Houve chuva sobre a terra quarenta dias e quarenta noites.",
      [("Sete", False), ("Quarenta", True), ("Cento e cinquenta", False), ("Doze", False)])
    q("Quem olhou para trás e virou estátua de sal?", "ANTIGO", "FACIL", "Gênesis 19:26", "Mulheres da Bíblia",
      "A mulher de Ló olhou para trás e ficou convertida numa estátua de sal.",
      [("Sara", False), ("Agar", False), ("A mulher de Ló", True), ("Rebeca", False)])
    q("Qual sobrinho de Abraão morou em Sodoma?", "ANTIGO", "FACIL", "Gênesis 13:12", "Gênesis",
      "Ló escolheu a planície do Jordão e habitou em Sodoma.",
      [("Labão", False), ("Ló", True), ("Ismael", False), ("Eliezer", False)])
    q("Qual filho nasceu a Adão e Eva depois da morte de Abel?", "ANTIGO", "FACIL", "Gênesis 4:25", "Gênesis",
      "Eva deu à luz um filho e o chamou Sete.",
      [("Caim", False), ("Enos", False), ("Sete", True), ("Enoque", False)])
    q("Quantos filhos Jacó teve?", "ANTIGO", "FACIL", "Gênesis 35:22", "Gênesis",
      "O texto diz que os filhos de Jacó foram doze.",
      [("Dez", False), ("Sete", False), ("Doze", True), ("Quatorze", False)])
    q("Quem foi a esposa de Isaque?", "ANTIGO", "FACIL", "Gênesis 24:67", "Mulheres da Bíblia",
      "Isaque tomou Rebeca por mulher.",
      [("Raquel", False), ("Lia", False), ("Rebeca", True), ("Zilpa", False)])
    q("Qual festa lembra a saída de Israel do Egito?", "ANTIGO", "FACIL", "Êxodo 12:11", "Êxodo",
      "A refeição da saída é chamada Páscoa do Senhor.",
      [("Pentecostes", False), ("Páscoa", True), ("Tabernáculos", False), ("Purim", False)])
    q("Segundo Gênesis 11, o que toda a terra tinha antes da torre de Babel?", "ANTIGO", "FACIL", "Gênesis 11:1", "Gênesis",
      "Toda a terra tinha uma só língua e uma só fala.",
      [("Uma só língua", True), ("Somente o hebraico escrito", False), ("Grego e aramaico", False), ("Nenhuma língua", False)])
    q("Qual menino ouviu a voz de Deus no templo e pensou que era Eli?", "ANTIGO", "FACIL", "1 Samuel 3:4", "Personagens bíblicos",
      "O Senhor chamou Samuel, e ele correu a Eli.",
      [("Davi", False), ("Samuel", True), ("Sansão", False), ("José", False)])
    q("Qual juíza e profetisa julgava Israel debaixo de uma palmeira?", "ANTIGO", "FACIL", "Juízes 4:4", "Mulheres da Bíblia",
      "Débora julgava Israel debaixo da palmeira de Débora.",
      [("Jael", False), ("Rute", False), ("Débora", True), ("Ester", False)])
    q("Qual é o último livro do Antigo Testamento na Bíblia protestante?", "ANTIGO", "FACIL", "Malaquias 1:1", "História bíblica",
      "Malaquias encerra o cânon protestante do Antigo Testamento.",
      [("Zacarias", False), ("Malaquias", True), ("Ageu", False), ("Sofonias", False)])
    q("Qual é o último livro da Bíblia?", "NOVO", "FACIL", "Apocalipse 1:1", "História bíblica",
      "O Apocalipse é o último livro do Novo Testamento.",
      [("Judas", False), ("Atos", False), ("Apocalipse", True), ("Hebreus", False)])
    q("Quantos livros tem a Bíblia usada pelas igrejas protestantes?", "ANTIGO", "FACIL", "Lucas 24:44", "História bíblica",
      "O cânon protestante reúne 39 livros do Antigo Testamento e 27 do Novo, 66 ao todo.",
      [("66", True), ("73", False), ("39", False), ("27", False)])
    q("Qual é o número do salmo que começa com: O Senhor é o meu pastor?", "ANTIGO", "FACIL", "Salmos 23:1", "Salmos",
      "O salmo 23 começa com essa frase.",
      [("1", False), ("51", False), ("23", True), ("119", False)])
    q("Segundo Provérbios 1:7, o temor do Senhor é o princípio de quê?", "ANTIGO", "FACIL", "Provérbios 1:7", "Provérbios",
      "O temor do Senhor é o princípio do conhecimento.",
      [("Das riquezas", False), ("Do conhecimento", True), ("Da guerra", False), ("Do templo", False)])
    q("De qual cidade José e Maria saíram para Belém?", "NOVO", "FACIL", "Lucas 2:4", "Vida de Jesus",
      "José subiu da Galileia, da cidade de Nazaré, para Belém.",
      [("Cafarnaum", False), ("Nazaré", True), ("Jerusalém", False), ("Jericó", False)])
    q("Qual rei procurou o menino Jesus para matá-lo?", "NOVO", "FACIL", "Mateus 2:16", "Vida de Jesus",
      "Herodes mandou matar os meninos de Belém.",
      [("Pilatos", False), ("César Augusto", False), ("Herodes", True), ("Agripa", False)])
    q("Quem era a mãe de João Batista?", "NOVO", "FACIL", "Lucas 1:13", "Mulheres da Bíblia",
      "O anjo disse a Zacarias que Isabel lhe daria um filho.",
      [("Maria", False), ("Ana", False), ("Isabel", True), ("Marta", False)])
    q("Qual apóstolo era cobrador de impostos?", "NOVO", "FACIL", "Mateus 9:9", "Apóstolos",
      "Jesus chamou Mateus, sentado na coletoria.",
      [("Pedro", False), ("Mateus", True), ("André", False), ("Filipe", False)])
    q("Qual era o ofício de Pedro antes de seguir Jesus?", "NOVO", "FACIL", "Mateus 4:18", "Apóstolos",
      "Jesus viu Simão e André lançando a rede ao mar, porque eram pescadores.",
      [("Carpinteiro", False), ("Pescador", True), ("Sacerdote", False), ("Cobrador de impostos", False)])
    q("Qual era o nome de Paulo antes de ser chamado Paulo?", "NOVO", "FACIL", "Atos 13:9", "Apóstolos",
      "Saulo, também chamado Paulo, é o mesmo homem.",
      [("Silas", False), ("Saulo", True), ("Timóteo", False), ("Barnabé", False)])
    q("Por quantos dias Jesus jejuou no deserto?", "NOVO", "FACIL", "Mateus 4:2", "Vida de Jesus",
      "Jesus jejuou quarenta dias e quarenta noites.",
      [("Três", False), ("Sete", False), ("Quarenta", True), ("Doze", False)])
    q("Onde Jesus pronunciou as bem-aventuranças, segundo Mateus 5?", "NOVO", "FACIL", "Mateus 5:1", "Vida de Jesus",
      "Vendo a multidão, subiu ao monte e começou a ensiná-la.",
      [("No monte", True), ("Dentro do templo", False), ("No palácio de Herodes", False), ("Num barco, somente", False)])
    q("Em qual evangelho Jesus diz: Eu sou o caminho, a verdade e a vida?", "NOVO", "FACIL", "João 14:6", "Vida de Jesus",
      "A frase está no evangelho de João.",
      [("Mateus", False), ("Marcos", False), ("Lucas", False), ("João", True)])
    q("Qual parábola fala de sementes que caem em solos diferentes?", "NOVO", "FACIL", "Mateus 13:3", "Parábolas",
      "Jesus contou a parábola do semeador.",
      [("A do semeador", True), ("A das dez virgens", False), ("A do filho pródigo", False), ("A da dracma", False)])
    q("Qual parábola fala de um pastor que vai atrás de uma ovelha?", "NOVO", "FACIL", "Lucas 15:4", "Parábolas",
      "O pastor deixa as noventa e nove para buscar a que se perdeu.",
      [("A do bom samaritano", False), ("A da ovelha perdida", True), ("A dos talentos", False), ("A do trigo e do joio", False)])
    q("Em qual festa o Espírito foi derramado em Atos 2?", "NOVO", "FACIL", "Atos 2:1", "Igreja primitiva",
      "Ao cumprir-se o dia de Pentecostes, estavam todos reunidos.",
      [("Páscoa", False), ("Pentecostes", True), ("Tabernáculos", False), ("Dedicação", False)])
    q("Em qual carta está escrito que o salário do pecado é a morte?", "NOVO", "FACIL", "Romanos 6:23", "Cartas paulinas",
      "Paulo escreve isso aos romanos.",
      [("Romanos", True), ("Filemom", False), ("Tiago", False), ("Judas", False)])
    q("Quantos peixes o rapaz tinha na multiplicação narrada em João 6?", "NOVO", "FACIL", "João 6:9", "Milagres",
      "O rapaz tinha cinco pães de cevada e dois peixes.",
      [("Um", False), ("Dois", True), ("Sete", False), ("Doze", False)])
    q("Em qual cidade Jesus transformou água em vinho?", "NOVO", "FACIL", "João 2:1", "Milagres",
      "Houve um casamento em Caná da Galileia.",
      [("Betsaida", False), ("Caná", True), ("Jericó", False), ("Emaús", False)])
    q("Em qual rio Jesus foi batizado?", "NOVO", "FACIL", "Mateus 3:13", "Vida de Jesus",
      "Jesus veio da Galileia ao Jordão para ser batizado.",
      [("Nilo", False), ("Eufrates", False), ("Jordão", True), ("Tigre", False)])
    q("Quantas vezes Pedro negou Jesus?", "NOVO", "FACIL", "Lucas 22:61", "Apóstolos",
      "Jesus havia dito que Pedro o negaria três vezes.",
      [("Uma", False), ("Duas", False), ("Três", True), ("Sete", False)])
    q("O que Pedro fez depois de negar Jesus e ouvir o galo?", "NOVO", "FACIL", "Lucas 22:62", "Apóstolos",
      "Pedro saiu e chorou amargamente.",
      [("Fugiu para Roma", False), ("Chorou", True), ("Voltou a pescar na mesma hora", False), ("Negou mais uma vez", False)])

    q("Quem se fez passar por Esaú para receber a bênção de Isaque?", "ANTIGO", "MEDIO", "Gênesis 27:19", "Gênesis",
      "Jacó disse ao pai: Eu sou Esaú, o teu primogênito.",
      [("Jacó", True), ("José", False), ("Labão", False), ("Ismael", False)])
    q("Quem era o pai de Abraão?", "ANTIGO", "MEDIO", "Gênesis 11:26", "Gênesis",
      "Terá gerou Abrão, Naor e Harã.",
      [("Naor", False), ("Terá", True), ("Harã", False), ("Sem", False)])
    q("De qual cidade Abraão saiu com seu pai, segundo Gênesis 11?", "ANTIGO", "MEDIO", "Gênesis 11:31", "Gênesis",
      "Terá saiu de Ur dos caldeus rumo a Canaã.",
      [("Harã", False), ("Ur dos caldeus", True), ("Damasco", False), ("Betel", False)])
    q("Sobre quais montes a arca de Noé repousou?", "ANTIGO", "MEDIO", "Gênesis 8:4", "Gênesis",
      "A arca repousou sobre os montes de Ararate.",
      [("Sinai", False), ("Carmelo", False), ("Ararate", True), ("Hermom", False)])
    q("Quem andou com Deus e foi tomado por ele, segundo Gênesis 5?", "ANTIGO", "MEDIO", "Gênesis 5:24", "Gênesis",
      "Enoque andou com Deus e já não foi achado, porque Deus o tomou.",
      [("Matusalém", False), ("Enoque", True), ("Noé", False), ("Sete", False)])
    q("Quantos anos viveu Matusalém?", "ANTIGO", "MEDIO", "Gênesis 5:27", "Gênesis",
      "Todos os dias de Matusalém foram novecentos e sessenta e nove anos.",
      [("777", False), ("930", False), ("969", True), ("120", False)])
    q("Em qual planície foi edificada a torre de Babel?", "ANTIGO", "MEDIO", "Gênesis 11:2", "Gênesis",
      "Acharam um vale na terra de Sinar e habitaram ali.",
      [("Sinar", True), ("Siquém", False), ("Moabe", False), ("Gileade", False)])
    q("Até quantos justos Abraão pediu, no fim, para que Sodoma fosse poupada?", "ANTIGO", "MEDIO", "Gênesis 18:32",
      "Gênesis", "Abraão parou o pedido em dez justos.",
      [("Cinquenta", False), ("Vinte", False), ("Dez", True), ("Um", False)])
    q("Por quantas peças de prata José foi vendido?", "ANTIGO", "MEDIO", "Gênesis 37:28", "Gênesis",
      "Os midianitas compraram José por vinte peças de prata.",
      [("Trinta", False), ("Vinte", True), ("Cem", False), ("Dez", False)])
    q("Quais eram os dois filhos de José nascidos no Egito?", "ANTIGO", "MEDIO", "Gênesis 41:51", "Gênesis",
      "José chamou o primogênito Manassés e o segundo Efraim.",
      [("Manassés e Efraim", True), ("Caim e Abel", False), ("Esaú e Jacó", False), ("Finéias e Eleazar", False)])
    q("Quantos espiões Moisés enviou a Canaã?", "ANTIGO", "MEDIO", "Números 13:2", "Antigo Testamento",
      "Moisés enviou um príncipe de cada tribo, doze homens.",
      [("Dois", False), ("Sete", False), ("Dez", False), ("Doze", True)])
    q("Qual espião, junto com Josué, confiou que Israel tomaria a terra?", "ANTIGO", "MEDIO", "Números 14:6", "Antigo Testamento",
      "Calebe e Josué rasgaram as vestes diante da congregação.",
      [("Corá", False), ("Calebe", True), ("Datã", False), ("Balaão", False)])
    q("Quem susteve os braços de Moisés na batalha contra Amaleque?", "ANTIGO", "MEDIO", "Êxodo 17:12", "Êxodo",
      "Arão e Hur sustentaram as mãos de Moisés.",
      [("Josué e Calebe", False), ("Arão e Hur", True), ("Nadabe e Abiú", False), ("Gérson e Eleazar", False)])
    q("Qual profeta desafiou os profetas de Baal no monte Carmelo?", "ANTIGO", "MEDIO", "1 Reis 18:21", "Profetas",
      "Elias reuniu o povo e os profetas de Baal no Carmelo.",
      [("Eliseu", False), ("Isaías", False), ("Elias", True), ("Jeremias", False)])
    q("Quantas vezes Elias mandou derramar água sobre o holocausto no Carmelo?", "ANTIGO", "MEDIO", "1 Reis 18:34", "Profetas",
      "Elias disse: fazei-o segunda vez; e terceira vez.",
      [("Uma", False), ("Duas", False), ("Três", True), ("Sete", False)])
    q("Qual rainha jurou matar Elias depois do Carmelo?", "ANTIGO", "MEDIO", "1 Reis 19:2", "Mulheres da Bíblia",
      "Jezabel mandou um mensageiro ameaçar Elias de morte.",
      [("Atalia", False), ("Jezabel", True), ("Bate-Seba", False), ("Mical", False)])
    q("Qual profeta repreendeu Davi por causa de Urias?", "ANTIGO", "MEDIO", "2 Samuel 12:7", "Profetas",
      "Natã disse a Davi: Tu és esse homem.",
      [("Samuel", False), ("Gade", False), ("Natã", True), ("Aías", False)])
    q("Quem era o marido de Bate-Seba?", "ANTIGO", "MEDIO", "2 Samuel 11:3", "Personagens bíblicos",
      "Bate-Seba era mulher de Urias, o heteu.",
      [("Urias", True), ("Joabe", False), ("Aitofel", False), ("Absalão", False)])
    q("Qual filho de Davi se rebelou para tomar o trono?", "ANTIGO", "MEDIO", "2 Samuel 15:6", "Reis de Israel",
      "Absalão roubou o coração dos homens de Israel.",
      [("Salomão", False), ("Amnom", False), ("Adonias", False), ("Absalão", True)])
    q("Qual rei consultou uma médium em En-Dor?", "ANTIGO", "MEDIO", "1 Samuel 28:7", "Reis de Israel",
      "Saul pediu que lhe buscassem uma mulher médium em En-Dor.",
      [("Davi", False), ("Saul", True), ("Acabe", False), ("Manassés", False)])
    q("Para qual cidade Deus mandou Jonas pregar?", "ANTIGO", "MEDIO", "Jonas 1:2", "Profetas",
      "A palavra do Senhor mandou Jonas ir a Nínive.",
      [("Babilônia", False), ("Tiro", False), ("Nínive", True), ("Damasco", False)])
    q("Qual rei sonhou com uma estátua de ouro, prata, bronze, ferro e barro?", "ANTIGO", "MEDIO", "Daniel 2:31", "Reis de Israel",
      "Daniel interpretou o sonho de Nabucodonosor.",
      [("Belsazar", False), ("Ciro", False), ("Dario", False), ("Nabucodonosor", True)])
    q("Quem leu a escrita na parede do palácio de Belsazar?", "ANTIGO", "MEDIO", "Daniel 5:17", "Profetas",
      "Daniel foi chamado para ler e interpretar a escrita.",
      [("Ezequiel", False), ("Daniel", True), ("Jeremias", False), ("Ageu", False)])
    q("Quem tramou destruir os judeus no livro de Ester?", "ANTIGO", "MEDIO", "Ester 3:6", "Antigo Testamento",
      "Hamã quis destruir todos os judeus do reino.",
      [("Mardoqueu", False), ("Hamã", True), ("Bigtã", False), ("Harbona", False)])
    q("Quem criou Ester depois da morte dos pais dela?", "ANTIGO", "MEDIO", "Ester 2:7", "Personagens bíblicos",
      "Mardoqueu criou Hadassa, isto é, Ester.",
      [("Hamã", False), ("Xerxes apenas", False), ("Mardoqueu", True), ("Neemias", False)])
    q("Qual livro diz que há tempo de nascer e tempo de morrer?", "ANTIGO", "MEDIO", "Eclesiastes 3:2", "Antigo Testamento",
      "Eclesiastes 3 lista os tempos debaixo do céu.",
      [("Jó", False), ("Eclesiastes", True), ("Cantares", False), ("Lamentações", False)])
    q("O livro de Eclesiastes apresenta o Pregador como filho de quem?", "ANTIGO", "MEDIO", "Eclesiastes 1:1", "Antigo Testamento",
      "O texto o chama de filho de Davi, rei em Jerusalém.",
      [("De Saul", False), ("De Jessé", False), ("De Davi", True), ("De Roboão", False)])
    q("Qual salmo é a oração de perdão depois do pecado de Davi com Bate-Seba?", "ANTIGO", "MEDIO", "Salmos 51:1", "Salmos",
      "O título do salmo 51 liga a oração à visita de Natã.",
      [("Salmo 23", False), ("Salmo 1", False), ("Salmo 119", False), ("Salmo 51", True)])
    q("Em qual capítulo de Provérbios está: Confia no Senhor de todo o teu coração?", "ANTIGO", "MEDIO", "Provérbios 3:5", "Provérbios",
      "A frase abre o conselho de Provérbios 3.",
      [("1", False), ("3", True), ("31", False), ("10", False)])
    q("Qual livro conta o sofrimento de um homem reto da terra de Uz?", "ANTIGO", "MEDIO", "Jó 1:1", "Antigo Testamento",
      "Havia um homem na terra de Uz, cujo nome era Jó.",
      [("Jó", True), ("Rute", False), ("Ester", False), ("Neemias", False)])
    q("Qual sacerdote ficou mudo até o nascimento do filho?", "NOVO", "MEDIO", "Lucas 1:20", "Personagens bíblicos",
      "Zacarias ficou mudo até o dia do nascimento de João.",
      [("Simeão", False), ("Zacarias", True), ("Caifás", False), ("Anás", False)])
    q("Quem instruiu a filha a pedir a cabeça de João Batista?", "NOVO", "MEDIO", "Mateus 14:8", "Mulheres da Bíblia",
      "Instruída pela mãe, a jovem pediu a cabeça de João. A mãe era Herodias.",
      [("Isabel", False), ("Maria Madalena", False), ("Herodias", True), ("Joana", False)])
    q("Na primeira tentação de Mateus 4, o que foi pedido a Jesus?", "NOVO", "MEDIO", "Mateus 4:3", "Vida de Jesus",
      "O tentador disse: manda que estas pedras se transformem em pães.",
      [("Transformar pedras em pães", True), ("Lançar-se do pináculo", False), ("Adorar o tentador", False), ("Acalmar o mar", False)])
    q("Quem foi obrigado a carregar a cruz de Jesus?", "NOVO", "MEDIO", "Mateus 27:32", "Vida de Jesus",
      "Os soldados encontraram Simão, cireneu, e o obrigaram a levar a cruz.",
      [("José de Arimateia", False), ("Nicodemos", False), ("Simão de Cirene", True), ("Barrabás", False)])
    q("Além de hebraico e latim, em qual idioma João diz que a inscrição da cruz foi escrita?", "NOVO", "MEDIO", "João 19:20", "Vida de Jesus",
      "Estava escrita em hebraico, latim e grego.",
      [("Grego", True), ("Aramaico, como quarto idioma", False), ("Egípcio", False), ("Persa", False)])
    q("Quem, ao pé da cruz, disse que Jesus era Filho de Deus, segundo Marcos?", "NOVO", "MEDIO", "Marcos 15:39", "Vida de Jesus",
      "O centurião que estava em frente de Jesus fez essa declaração.",
      [("Pilatos", False), ("O centurião", True), ("Herodes", False), ("Caifás", False)])
    q("Quem entrou primeiro no túmulo vazio, segundo João 20?", "NOVO", "MEDIO", "João 20:6", "Apóstolos",
      "Simão Pedro chegou depois e entrou no túmulo.",
      [("O outro discípulo", False), ("Pedro", True), ("Tomé", False), ("Maria Madalena", False)])
    q("Por quantos dias o ressuscitado se apresentou aos apóstolos, segundo Atos 1?", "NOVO", "MEDIO", "Atos 1:3", "Igreja primitiva",
      "Por quarenta dias apareceu-lhes e falou do reino de Deus.",
      [("Três", False), ("Sete", False), ("Quarenta", True), ("Cinquenta", False)])
    q("Cerca de quantas pessoas foram batizadas no dia de Pentecostes?", "NOVO", "MEDIO", "Atos 2:41", "Igreja primitiva",
      "Naquele dia agregaram-se quase três mil pessoas.",
      [("Quinhentas", False), ("Três mil", True), ("Cinco mil", False), ("Cento e vinte", False)])
    q("Aos pés de quem foram postas as vestes dos que apedrejaram Estêvão?", "NOVO", "MEDIO", "Atos 7:58", "Igreja primitiva",
      "As testemunhas deixaram as vestes aos pés de um jovem chamado Saulo.",
      [("Pedro", False), ("Saulo", True), ("Filipe", False), ("Barnabé", False)])
    q("Quem batizou o eunuco etíope?", "NOVO", "MEDIO", "Atos 8:38", "Igreja primitiva",
      "Filipe batizou o eunuco.",
      [("Pedro", False), ("Paulo", False), ("Filipe", True), ("Ananias", False)])
    q("Quem impôs as mãos em Saulo, em Damasco, para que voltasse a ver?", "NOVO", "MEDIO", "Atos 9:17", "Igreja primitiva",
      "Ananias entrou, impôs as mãos sobre Saulo e ele recuperou a vista.",
      [("Pedro", False), ("Barnabé", False), ("Ananias", True), ("Gamaliel", False)])
    q("Quem teve a visão de um lençol com animais em Jope?", "NOVO", "MEDIO", "Atos 10:11", "Apóstolos",
      "Pedro viu o céu aberto e um lençol que descia.",
      [("Cornélio", False), ("Pedro", True), ("João", False), ("Filipe", False)])
    q("Qual outro nome de Dorcas?", "NOVO", "MEDIO", "Atos 9:36", "Mulheres da Bíblia",
      "Havia em Jope uma discípula chamada Tabita, que traduzido é Dorcas.",
      [("Lídia", False), ("Priscila", False), ("Tabita", True), ("Febe", False)])
    q("Em qual cidade Pedro ressuscitou Tabita?", "NOVO", "MEDIO", "Atos 9:36", "Milagres",
      "Tabita vivia em Jope, e Pedro foi chamado para lá.",
      [("Jope", True), ("Cesareia", False), ("Antioquia", False), ("Lida, onde curou Eneas", False)])
    q("Quem vendeu um campo e entregou o valor aos apóstolos?", "NOVO", "MEDIO", "Atos 4:37", "Igreja primitiva",
      "José, chamado Barnabé, vendeu um campo e trouxe o dinheiro.",
      [("Ananias", False), ("Barnabé", True), ("Cornélio", False), ("Áquila", False)])
    q("De qual cidade Paulo e Barnabé partiram na primeira viagem missionária?", "NOVO", "MEDIO", "Atos 13:4", "Viagens missionárias",
      "Enviados pelo Espírito, desceram a Selêucia saindo de Antioquia.",
      [("Jerusalém", False), ("Antioquia", True), ("Tarso", False), ("Filipos", False)])
    q("Quem acompanhou Paulo na segunda viagem depois da separação de Barnabé?", "NOVO", "MEDIO", "Atos 15:40", "Viagens missionárias",
      "Paulo escolheu Silas e partiu.",
      [("João Marcos", False), ("Silas", True), ("Timóteo", False), ("Apolo", False)])
    q("Em qual cidade Paulo e Silas cantaram na prisão à noite?", "NOVO", "MEDIO", "Atos 16:25", "Viagens missionárias",
      "Em Filipos, Paulo e Silas oravam e cantavam na prisão.",
      [("Tessalônica", False), ("Bereia", False), ("Filipos", True), ("Corinto", False)])
    q("Em qual cidade Paulo falou no Areópago?", "NOVO", "MEDIO", "Atos 17:22", "Viagens missionárias",
      "Paulo, em pé no Areópago, falou aos atenienses.",
      [("Atenas", True), ("Corinto", False), ("Éfeso", False), ("Roma", False)])
    q("Os moradores de qual cidade examinavam as Escrituras todos os dias?", "NOVO", "MEDIO", "Atos 17:11", "Igreja primitiva",
      "Os de Bereia eram mais nobres e examinavam as Escrituras diariamente.",
      [("Atenas", False), ("Bereia", True), ("Corinto", False), ("Éfeso", False)])
    q("De qual deus Paulo foi chamado em Listra?", "NOVO", "MEDIO", "Atos 14:12", "Viagens missionárias",
      "A Barnabé chamaram Zeus e a Paulo, Hermes.",
      [("Zeus", False), ("Hermes", True), ("Ártemis", False), ("Apolo", False)])
    q("Qual casal fabricava tendas com Paulo em Corinto?", "NOVO", "MEDIO", "Atos 18:3", "Igreja primitiva",
      "Paulo ficou com Áquila e Priscila, pois eram do mesmo ofício.",
      [("Ananias e Safira", False), ("Áquila e Priscila", True), ("André e Pedro", False), ("Marta e Maria", False)])
    q("Em qual cidade os ourives de Ártemis se levantaram contra Paulo?", "NOVO", "MEDIO", "Atos 19:23", "Viagens missionárias",
      "O tumulto dos ourives aconteceu em Éfeso.",
      [("Filipos", False), ("Atenas", False), ("Éfeso", True), ("Mileto", False)])
    q("Qual rei disse a Paulo que por pouco se fazia cristão?", "NOVO", "MEDIO", "Atos 26:28", "Viagens missionárias",
      "Agripa respondeu a Paulo com essa frase.",
      [("Festo", False), ("Agripa", True), ("Cláudio", False), ("Nero", False)])
    q("Para quem Paulo apelou no tribunal?", "NOVO", "MEDIO", "Atos 25:11", "Viagens missionárias",
      "Paulo disse: apelo para César.",
      [("Para o sinédrio", False), ("Para César", True), ("Para Agripa", False), ("Para o procônsul de Chipre", False)])
    q("Qual animal mordeu a mão de Paulo em Malta?", "NOVO", "MEDIO", "Atos 28:3", "Viagens missionárias",
      "Uma víbora saiu do calor e prendeu-se à mão de Paulo.",
      [("Um escorpião", False), ("Uma víbora", True), ("Um leão", False), ("Um cão", False)])
    q("Quem deu a palavra final no concílio de Jerusalém?", "NOVO", "MEDIO", "Atos 15:13", "Igreja primitiva",
      "Depois de Barnabé e Paulo, Tiago tomou a palavra e concluiu.",
      [("Pedro", False), ("Paulo", False), ("Tiago", True), ("Silas", False)])
    q("O concílio de Jerusalém exigiu circuncidar os gentios convertidos?", "NOVO", "MEDIO", "Atos 15:19", "Igreja primitiva",
      "Tiago concluiu que não se devia perturbar os gentios que se convertiam, impondo-lhes a circuncisão.",
      [("Sim, como condição de salvação", False), ("Não", True), ("Somente os de Antioquia", False), ("Somente os de Jerusalém", False)])
    q("Qual discípulo cortou a orelha do servo do sumo sacerdote?", "NOVO", "MEDIO", "João 18:10", "Apóstolos",
      "Simão Pedro puxou a espada e feriu o servo.",
      [("Tiago", False), ("João", False), ("Pedro", True), ("Tomé", False)])
    q("Quem ungiu os pés de Jesus em Betânia, segundo João 12?", "NOVO", "MEDIO", "João 12:3", "Mulheres da Bíblia",
      "Maria tomou uma libra de bálsamo e ungiu os pés de Jesus.",
      [("Marta", False), ("Maria", True), ("Joana", False), ("Susana", False)])
    q("Quem era a irmã de Maria e de Lázaro?", "NOVO", "MEDIO", "João 11:1", "Mulheres da Bíblia",
      "Lázaro, de Betânia, era irmão de Marta e de Maria.",
      [("Marta", True), ("Isabel", False), ("Joana", False), ("Susana", False)])
    q("Havia quantos dias Lázaro estava no túmulo quando Jesus chegou?", "NOVO", "MEDIO", "João 11:17", "Milagres",
      "Jesus encontrou Lázaro já sepultado havia quatro dias.",
      [("Dois", False), ("Três", False), ("Quatro", True), ("Sete", False)])
    q("Qual parábola fala de dez virgens e suas lâmpadas?", "NOVO", "MEDIO", "Mateus 25:1", "Parábolas",
      "O reino dos céus é comparado a dez virgens.",
      [("A das dez virgens", True), ("A do joio", False), ("A da rede", False), ("A do fermento", False)])
    q("Qual parábola fala de servos que receberam quantias para negociar?", "NOVO", "MEDIO", "Mateus 25:14", "Parábolas",
      "Um homem, ao partir, entregou seus bens aos servos: a parábola dos talentos.",
      [("A do credor incompassivo", False), ("A dos talentos", True), ("A da figueira", False), ("A dos lavradores", False)])
    q("Em qual capítulo de Mateus está a oração do Pai-Nosso?", "NOVO", "MEDIO", "Mateus 6:9", "Vida de Jesus",
      "Jesus ensina a orar no sermão do monte, no capítulo 6.",
      [("5", False), ("6", True), ("13", False), ("28", False)])
    q("Em qual evangelho Jesus diz: Eu sou a videira verdadeira?", "NOVO", "MEDIO", "João 15:1", "Vida de Jesus",
      "A declaração está no evangelho de João.",
      [("Mateus", False), ("Marcos", False), ("Lucas", False), ("João", True)])
    q("A quem Jesus disse: hoje estarás comigo no paraíso?", "NOVO", "MEDIO", "Lucas 23:43", "Vida de Jesus",
      "Jesus respondeu ao malfeitor que lhe pediu que se lembrasse dele.",
      [("Ao centurião", False), ("A Pilatos", False), ("Ao malfeitor arrependido", True), ("A José de Arimateia", False)])
    q("Em qual carta Paulo descreve a armadura de Deus?", "NOVO", "MEDIO", "Efésios 6:11", "Cartas paulinas",
      "A armadura de Deus está na carta aos efésios.",
      [("Romanos", False), ("Efésios", True), ("Colossenses", False), ("Filipenses", False)])
    q("Em qual capítulo de 1 Coríntios Paulo descreve o amor?", "NOVO", "MEDIO", "1 Coríntios 13:4", "Cartas paulinas",
      "O capítulo 13 fala do amor que é paciente e bondoso.",
      [("11", False), ("12", False), ("13", True), ("15", False)])
    q("Em qual carta Paulo escreve: já não sou eu quem vive, mas Cristo vive em mim?", "NOVO", "MEDIO", "Gálatas 2:20", "Cartas paulinas",
      "A frase está na carta aos gálatas.",
      [("Gálatas", True), ("Efésios", False), ("Filemom", False), ("Tito", False)])
    q("A quem Paulo chama de verdadeiro filho na fé, logo no início de 1 Timóteo?", "NOVO", "MEDIO", "1 Timóteo 1:2", "Cartas paulinas",
      "A carta é endereçada a Timóteo, verdadeiro filho na fé.",
      [("Tito", False), ("Timóteo", True), ("Silas", False), ("Onésimo", False)])
    q("Qual evangelho é dedicado a Teófilo?", "NOVO", "MEDIO", "Lucas 1:3", "História bíblica",
      "Lucas escreve a Teófilo no prólogo do evangelho.",
      [("Mateus", False), ("Marcos", False), ("Lucas", True), ("João", False)])
    q("Segundo Tiago, a fé sem obras é o quê?", "NOVO", "MEDIO", "Tiago 2:17", "Novo Testamento",
      "A fé, se não tiver obras, é morta em si mesma.",
      [("Perfeita", False), ("Morta", True), ("Opcional", False), ("Maior que o amor", False)])
    q("Qual capítulo de Hebreus reúne os exemplos de fé?", "NOVO", "MEDIO", "Hebreus 11:1", "Novo Testamento",
      "Hebreus 11 percorre os que viveram pela fé.",
      [("1", False), ("4", False), ("11", True), ("13", False)])
    q("Segundo Hebreus 11, quem ofereceu Isaque pela fé?", "NOVO", "MEDIO", "Hebreus 11:17", "Personagens bíblicos",
      "Pela fé Abraão ofereceu Isaque quando foi provado.",
      [("Jacó", False), ("Abraão", True), ("Moisés", False), ("Davi", False)])
    q("Qual comandante sírio foi curado da lepra ao banhar-se no Jordão?", "ANTIGO", "MEDIO", "2 Reis 5:14", "Profetas",
      "Naamã desceu ao Jordão sete vezes e ficou limpo.",
      [("Ben-Hadade", False), ("Naamã", True), ("Hazael", False), ("Senaqueribe", False)])
    q("Qual rei de Judá, doente, recebeu mais quinze anos de vida?", "ANTIGO", "MEDIO", "2 Reis 20:6", "Reis de Israel",
      "O Senhor disse a Ezequias que acrescentaria quinze anos aos seus dias.",
      [("Josias", False), ("Acaz", False), ("Ezequias", True), ("Manassés", False)])
    q("De quem era o campo em que Rute respigou?", "ANTIGO", "MEDIO", "Rute 2:3", "Mulheres da Bíblia",
      "Rute veio ao campo de Boaz.",
      [("De Elimeleque apenas", False), ("De Boaz", True), ("De Noemi", False), ("De Orfa", False)])
    q("Qual profeta teve os lábios tocados por uma brasa do altar?", "ANTIGO", "MEDIO", "Isaías 6:6", "Profetas",
      "Um serafim tocou a boca de Isaías com uma brasa.",
      [("Jeremias", False), ("Ezequiel", False), ("Isaías", True), ("Oséias", False)])
    q("Qual nome da profecia citada em Mateus significa Deus conosco?", "NOVO", "MEDIO", "Mateus 1:23", "Vida de Jesus",
      "Emanuel é traduzido: Deus conosco.",
      [("Jesus, nesta frase", False), ("Emanuel", True), ("Messias", False), ("Cristo", False)])
    q("Qual nome Jacó recebeu depois de lutar em Peniel?", "ANTIGO", "MEDIO", "Gênesis 32:28", "Gênesis",
      "Foi-lhe dito: já não te chamarás Jacó, mas Israel.",
      [("Israel", True), ("Esaú", False), ("Abrão", False), ("Peniel", False)])
    q("Qual rei da Pérsia autorizou a reconstrução do templo em Jerusalém?", "ANTIGO", "MEDIO", "Esdras 1:1", "Reis de Israel",
      "Ciro despertou o espírito para reedificar a casa do Senhor.",
      [("Dario", False), ("Artaxerxes", False), ("Ciro", True), ("Assuero", False)])
    q("Quem leu a Lei diante do povo no livro de Neemias?", "ANTIGO", "MEDIO", "Neemias 8:2", "Antigo Testamento",
      "Esdras, o escriba, trouxe o livro da Lei diante da congregação.",
      [("Neemias", False), ("Esdras", True), ("Zorobabel", False), ("Josué, o sumo sacerdote", False)])
    q("Rute foi ancestral direta de qual rei, segundo Rute 4?", "ANTIGO", "MEDIO", "Rute 4:17", "Mulheres da Bíblia",
      "Obede, filho de Rute, foi pai de Jessé, pai de Davi.",
      [("Saul", False), ("Salomão, como filho dela", False), ("Davi", True), ("Ezequias", False)])

    q("Qual era o nome hebraico de Ester?", "ANTIGO", "DIFICIL", "Ester 2:7", "Mulheres da Bíblia",
      "Ester também se chamava Hadassa.",
      [("Hadassa", True), ("Zeres", False), ("Vasti", False), ("Abiail, que era o pai", False)])
    q("Quem foi a esposa egípcia de José?", "ANTIGO", "DIFICIL", "Gênesis 41:45", "Gênesis",
      "Faraó deu a José por mulher Asenate, filha de Potífera.",
      [("Hagar", False), ("Asenate", True), ("Quetura", False), ("Bila", False)])
    q("Quem reconstruiu Jericó no tempo de Acabe e perdeu os filhos, como Josué dissera?", "ANTIGO", "DIFICIL", "1 Reis 16:34", "Reis de Israel",
      "Hiel, o betelita, edificou Jericó.",
      [("Onri", False), ("Hiel", True), ("Zimri", False), ("Tibni", False)])
    q("Qual rei ficou leproso ao queimar incenso no templo?", "ANTIGO", "DIFICIL", "2 Crônicas 26:19", "Reis de Israel",
      "A lepra apareceu na testa de Uzias diante dos sacerdotes.",
      [("Uzias", True), ("Jotão", False), ("Acaz", False), ("Ezequias", False)])
    q("Com quantos anos Joás começou a reinar em Judá?", "ANTIGO", "DIFICIL", "2 Reis 11:21", "Reis de Israel",
      "Tinha Joás sete anos quando começou a reinar.",
      [("Oito", False), ("Doze", False), ("Sete", True), ("Três", False)])
    q("Com quantos anos Josias começou a reinar?", "ANTIGO", "DIFICIL", "2 Reis 22:1", "Reis de Israel",
      "Josias tinha oito anos quando começou a reinar.",
      [("Oito", True), ("Dezesseis", False), ("Vinte e cinco", False), ("Sete", False)])
    q("Quem foi o pai de Boaz na genealogia de Rute 4?", "ANTIGO", "DIFICIL", "Rute 4:21", "Antigo Testamento",
      "Salmom gerou Boaz.",
      [("Naassom", False), ("Salmom", True), ("Obede", False), ("Jessé", False)])
    q("Qual cidade Calebe recebeu por herança?", "ANTIGO", "DIFICIL", "Josué 14:14", "Antigo Testamento",
      "Hebrom ficou sendo de Calebe.",
      [("Jericó", False), ("Siló", False), ("Hebrom", True), ("Laís", False)])
    q("Quais três amigos vieram consolar Jó?", "ANTIGO", "DIFICIL", "Jó 2:11", "Personagens bíblicos",
      "Elifaz, Bildade e Zofar combinaram ir consolá-lo.",
      [("Elifaz, Bildade e Zofar", True), ("Natã, Gade e Aías", False), ("Sadraque, Mesaque e Abede-Nego", False), ("Sem, Cam e Jafé", False)])
    q("Qual jovem Paulo não quis levar na segunda viagem missionária?", "NOVO", "DIFICIL", "Atos 15:38", "Viagens missionárias",
      "Paulo não achava justo levar João Marcos, que os havia deixado.",
      [("Silas", False), ("Timóteo", False), ("João Marcos", True), ("Tito", False)])
    q("De qual cidade era Timóteo?", "NOVO", "DIFICIL", "Atos 16:1", "Viagens missionárias",
      "Paulo chegou a Derbe e Listra, e estava ali um discípulo chamado Timóteo.",
      [("Tarso", False), ("Listra", True), ("Antioquia", False), ("Filipos", False)])
    q("Quem era a mãe de Timóteo?", "NOVO", "DIFICIL", "2 Timóteo 1:5", "Mulheres da Bíblia",
      "Paulo lembra a fé de Loide e de Eunice, mãe de Timóteo.",
      [("Loide", False), ("Eunice", True), ("Priscila", False), ("Febe", False)])
    q("Quem era a avó de Timóteo?", "NOVO", "DIFICIL", "2 Timóteo 1:5", "Mulheres da Bíblia",
      "A fé habitou primeiro em sua avó Loide.",
      [("Loide", True), ("Eunice", False), ("Lídia", False), ("Dorcas", False)])
    q("Quem Paulo deixou doente em Mileto?", "NOVO", "DIFICIL", "2 Timóteo 4:20", "Cartas paulinas",
      "Paulo escreve que deixou Trófimo doente em Mileto.",
      [("Tíquico", False), ("Trófimo", True), ("Aristarco", False), ("Gaio", False)])
    q("Qual ourives incitou o tumulto em Éfeso?", "NOVO", "DIFICIL", "Atos 19:24", "Viagens missionárias",
      "Um ourives chamado Demétrio fazia nichos de Ártemis.",
      [("Demétrio", True), ("Alexandre", False), ("Erasto", False), ("Tertuliano, nome posterior", False)])
    q("Qual era o outro nome do mágico Barjesus, em Chipre?", "NOVO", "DIFICIL", "Atos 13:8", "Viagens missionárias",
      "Elimas, o mágico, é o mesmo Barjesus.",
      [("Elimas", True), ("Simão, o mago de Samaria", False), ("Hermes", False), ("Ágabo", False)])
    q("A serviço de qual rainha estava o eunuco etíope?", "NOVO", "DIFICIL", "Atos 8:27", "Igreja primitiva",
      "Ele era oficial de Candace, rainha dos etíopes.",
      [("Vasti", False), ("Candace", True), ("Berenice", False), ("Drusila", False)])
    q("Na parábola de Mateus 25, quantos talentos recebeu o primeiro servo?", "NOVO", "DIFICIL", "Mateus 25:15", "Parábolas",
      "A um deu cinco talentos, a outro dois e a outro um.",
      [("Dez", False), ("Cinco", True), ("Dois", False), ("Um", False)])
    q("De qual tribo Paulo dizia ser?", "NOVO", "DIFICIL", "Filipenses 3:5", "Cartas paulinas",
      "Paulo se apresenta como da tribo de Benjamim.",
      [("Judá", False), ("Levi", False), ("Benjamim", True), ("Efraim", False)])
    q("Em qual ilha Paulo deixou Tito?", "NOVO", "DIFICIL", "Tito 1:5", "Cartas paulinas",
      "Paulo deixou Tito em Creta para corrigir o que faltava.",
      [("Malta", False), ("Chipre", False), ("Creta", True), ("Patmos", False)])
    q("Segundo Atos 1, desde qual acontecimento o substituto de Judas precisava ter acompanhado Jesus?", "NOVO", "DIFICIL", "Atos 1:22", "Apóstolos",
      "Era necessário ter acompanhado o Senhor desde o batismo de João até a ascensão.",
      [("Desde o nascimento em Belém", False), ("Desde o batismo de João", True), ("Desde a transfiguração", False), ("Desde a última ceia", False)])
    q("Qual era o outro nome de Barnabé?", "NOVO", "DIFICIL", "Atos 4:36", "Apóstolos",
      "José, a quem os apóstolos chamaram Barnabé.",
      [("José", True), ("João", False), ("Silas", False), ("Manaém", False)])
    q("Qual imperador havia mandado sair de Roma todos os judeus, segundo Atos 18?", "NOVO", "DIFICIL", "Atos 18:2", "História bíblica",
      "Cláudio tinha decretado que todos os judeus saíssem de Roma.",
      [("Augusto", False), ("Tibério", False), ("Cláudio", True), ("Nero", False)])
    q("A quem Paulo resistiu face a face em Antioquia, segundo Gálatas 2?", "NOVO", "DIFICIL", "Gálatas 2:11", "Cartas paulinas",
      "Quando Cefas veio a Antioquia, Paulo lhe resistiu face a face.",
      [("Cefas", True), ("Barnabé", False), ("Tiago", False), ("Apolo", False)])
    q("Por quanto tempo Elias orou para que não chovesse, segundo Tiago 5?", "NOVO", "DIFICIL", "Tiago 5:17", "Novo Testamento",
      "Elias orou para que não chovesse, e não choveu por três anos e seis meses.",
      [("Um ano", False), ("Três anos", False), ("Três anos e seis meses", True), ("Sete anos", False)])
    q("Quem se apresenta como servo de Jesus Cristo e irmão de Tiago?", "NOVO", "DIFICIL", "Judas 1:1", "Novo Testamento",
      "Assim começa a carta de Judas.",
      [("Pedro", False), ("João", False), ("Judas", True), ("Barnabé", False)])
    q("Quantos capítulos tem a carta de Judas?", "NOVO", "DIFICIL", "Judas 1:1", "Novo Testamento",
      "A carta de Judas é um único capítulo.",
      [("Um", True), ("Dois", False), ("Três", False), ("Cinco", False)])
    q("Quem liderou a rebelião em que a terra engoliu também Datã e Abirão?", "ANTIGO", "DIFICIL", "Números 16:32", "Antigo Testamento",
      "A terra abriu a boca e tragou Corá, Datã e Abirão.",
      [("Corá", True), ("Balaão", False), ("Finéias", False), ("Calebe", False)])
    q("Qual rei chamou Balaão para amaldiçoar Israel?", "ANTIGO", "DIFICIL", "Números 22:5", "Antigo Testamento",
      "Balaque, rei de Moabe, mandou chamar Balaão.",
      [("Sio", False), ("Balaque", True), ("Ogue", False), ("Eglom", False)])
    q("Quem era rei de Salém e sacerdote, e abençoou Abraão?", "ANTIGO", "DIFICIL", "Gênesis 14:18", "Gênesis",
      "Melquisedeque, rei de Salém, trouxe pão e vinho e abençoou Abrão.",
      [("Abimeleque", False), ("Melquisedeque", True), ("Jetro", False), ("Potífera", False)])
    q("Qual profeta, junto com Zacarias, animou a reconstrução do templo depois do exílio?", "ANTIGO", "DIFICIL", "Esdras 5:1", "Profetas",
      "Ageu e Zacarias profetizaram aos judeus que estavam em Judá.",
      [("Ageu", True), ("Malaquias", False), ("Joel", False), ("Obadias", False)])
    q("Quem liderou o lançamento dos alicerces do segundo templo no retorno?", "ANTIGO", "DIFICIL", "Esdras 3:8", "História bíblica",
      "Zorobabel e Jesua começaram a obra da casa do Senhor.",
      [("Neemias", False), ("Zorobabel", True), ("Ciro", False), ("Mardoqueu", False)])
    q("Quem Jezabel mandou matar para que Acabe tomasse a vinha?", "ANTIGO", "DIFICIL", "1 Reis 21:15", "Reis de Israel",
      "Nabote foi morto, e Acabe tomou a vinha.",
      [("Nabote", True), ("Obadias", False), ("Micaías", False), ("Elias", False)])
    q("Qual servo de Eliseu ficou leproso depois de pedir prata a Naamã?", "ANTIGO", "DIFICIL", "2 Reis 5:27", "Profetas",
      "A lepra de Naamã se apegou a Geazi.",
      [("Obadias", False), ("Geazi", True), ("Micaías", False), ("Baraque", False)])
    q("Quem escondeu cem profetas em cavernas no tempo de Jezabel?", "ANTIGO", "DIFICIL", "1 Reis 18:4", "Profetas",
      "Obadias escondeu cem profetas, cinquenta em cada caverna.",
      [("Elias", False), ("Eliseu", False), ("Obadias", True), ("Micaías", False)])
    q("Qual profeta anunciou a derrota de Acabe em Ramote-Gileade, contra o coro da corte?", "ANTIGO", "DIFICIL", "1 Reis 22:17", "Profetas",
      "Micaías viu Israel espalhado como ovelhas sem pastor.",
      [("Elias", False), ("Micaías", True), ("Eliseu", False), ("Jonas", False)])
    q("Qual filho de Ezequias se humilhou depois de reinar com violência, segundo 2 Crônicas?", "ANTIGO", "DIFICIL", "2 Crônicas 33:12", "Reis de Israel",
      "Manassés, angustiado, humilhou-se grandemente perante o Deus de seus pais.",
      [("Manassés", True), ("Amom", False), ("Josias", False), ("Joacaz", False)])
    q("Qual concunhada de Rute voltou ao seu povo em Moabe?", "ANTIGO", "DIFICIL", "Rute 1:14", "Mulheres da Bíblia",
      "Orfa beijou a sogra e voltou; Rute ficou.",
      [("Noemi", False), ("Orfa", True), ("Rute", False), ("Mara", False)])
    q("Qual jovem falou a Jó depois dos três amigos?", "ANTIGO", "DIFICIL", "Jó 32:2", "Personagens bíblicos",
      "Eliú, filho de Baraquel, esperou para falar.",
      [("Eliú", True), ("Eli", False), ("Elifaz", False), ("Bildade", False)])
    q("Na visão de Ezequiel, os seres viventes tinham rosto de homem, de leão, de boi e de qual animal?", "ANTIGO", "DIFICIL", "Ezequiel 1:10", "Profetas",
      "O quarto rosto era de águia.",
      [("Serpente", False), ("Cordeiro", False), ("Águia", True), ("Cavalo", False)])
    q("Qual profeta anunciou que de Belém sairia o governante de Israel?", "ANTIGO", "DIFICIL", "Miquéias 5:2", "Profetas",
      "Miquéias aponta Belém Efrata como origem do governante.",
      [("Isaías", False), ("Miquéias", True), ("Amós", False), ("Naum", False)])
    q("Com quantos anos Abraão saiu de Harã?", "ANTIGO", "DIFICIL", "Gênesis 12:4", "Gênesis",
      "Abrão tinha setenta e cinco anos quando saiu de Harã.",
      [("Setenta e cinco", True), ("Noventa", False), ("Cem", False), ("Sessenta", False)])
    q("Abraão diz, em Gênesis 17, que Sara tinha quantos anos?", "ANTIGO", "DIFICIL", "Gênesis 17:17", "Gênesis",
      "Abraão pergunta se a Sara, de noventa anos, haveria de dar à luz.",
      [("Setenta", False), ("Oitenta", False), ("Noventa", True), ("Cem", False)])
    q("Quem pediu que seus ossos fossem levados quando Israel saísse do Egito?", "ANTIGO", "DIFICIL", "Gênesis 50:25", "Gênesis",
      "José fez os filhos de Israel jurarem que levariam os seus ossos.",
      [("Jacó", False), ("José", True), ("Efraim", False), ("Manassés", False)])
    q("Qual neto de Arão deteve a praga ao atravessar o casal que profanava o arraial?", "ANTIGO", "DIFICIL", "Números 25:7", "Antigo Testamento",
      "Finéias, filho de Eleazar, levantou-se no meio da congregação.",
      [("Nadabe", False), ("Abiú", False), ("Finéias", True), ("Itamar", False)])

add_lote()
destino = Path(__file__).resolve().parents[1] / "src" / "main" / "resources" / "db" / "perguntas-extra.sql"
linhas = [
    "-- Perguntas complementares. Idempotente: nao reinsere enunciado nem chave de seed.",
    f"-- Quantidade neste arquivo: {len(P)}",
    "",
    "CREATE TEMP TABLE seed_pergunta (",
    "    chave integer PRIMARY KEY,",
    "    enunciado varchar(500) NOT NULL,",
    "    testamento varchar(10) NOT NULL CHECK (testamento IN ('ANTIGO', 'NOVO')),",
    "    nivel varchar(10) NOT NULL CHECK (nivel IN ('FACIL', 'MEDIO', 'DIFICIL')),",
    "    referencia varchar(120) NOT NULL,",
    "    categoria varchar(80) NOT NULL,",
    "    explicacao varchar(500)",
    ");",
    "",
    "CREATE TEMP TABLE seed_alternativa (",
    "    chave integer NOT NULL REFERENCES seed_pergunta (chave),",
    "    texto varchar(255) NOT NULL,",
    "    correta boolean NOT NULL,",
    "    ordem smallint NOT NULL CHECK (ordem BETWEEN 1 AND 4),",
    "    PRIMARY KEY (chave, ordem)",
    ");",
    "",
    "INSERT INTO seed_pergunta (chave, enunciado, testamento, nivel, referencia, categoria, explicacao) VALUES",
]
valores = []
alternativas = []
vistos = set()
for indice, item in enumerate(P, start=201):
    enunciado, testamento, nivel, referencia, categoria, explicacao, opcoes = item
    chave_norm = enunciado.strip().lower()
    if chave_norm in vistos:
        raise SystemExit("Enunciado duplicado: " + enunciado)
    vistos.add(chave_norm)
    def esc(texto):
        return texto.replace("'", "''")
    valores.append(
        f"    ({indice}, '{esc(enunciado)}', '{testamento}', '{nivel}', '{esc(referencia)}', '{esc(categoria)}', '{esc(explicacao)}')"
    )
    for ordem, (texto, correta) in enumerate(opcoes, start=1):
        alternativas.append(
            f"    ({indice}, '{esc(texto)}', {'true' if correta else 'false'}, {ordem})"
        )
linhas.append(",\n".join(valores) + ";")
linhas.append("")
linhas.append("INSERT INTO seed_alternativa (chave, texto, correta, ordem) VALUES")
linhas.append(",\n".join(alternativas) + ";")
linhas.append("""
DO $$
DECLARE
    r RECORD;
    existente_id bigint;
    novo_id bigint;
    qtd_certas integer;
    qtd_alts integer;
BEGIN
    FOR r IN SELECT * FROM seed_pergunta ORDER BY chave LOOP
        SELECT COUNT(*) INTO qtd_certas FROM seed_alternativa WHERE chave = r.chave AND correta;
        SELECT COUNT(*) INTO qtd_alts FROM seed_alternativa WHERE chave = r.chave;
        IF qtd_certas <> 1 OR qtd_alts <> 4 THEN
            RAISE EXCEPTION 'A pergunta % precisa de 4 alternativas e uma unica correta', r.chave;
        END IF;

        SELECT p.id INTO existente_id
        FROM pergunta p
        WHERE p.chave_seed = 'seed-' || r.chave
           OR lower(btrim(p.enunciado)) = lower(btrim(r.enunciado))
        ORDER BY CASE WHEN p.chave_seed = 'seed-' || r.chave THEN 0 ELSE 1 END, p.id
        LIMIT 1;

        IF existente_id IS NOT NULL THEN
            UPDATE pergunta
            SET chave_seed = 'seed-' || r.chave,
                status = COALESCE(status, 'APROVADA'),
                categoria = COALESCE(categoria, r.categoria),
                explicacao = COALESCE(explicacao, r.explicacao)
            WHERE id = existente_id
              AND chave_seed IS NULL
              AND NOT EXISTS (
                  SELECT 1 FROM pergunta outra WHERE outra.chave_seed = 'seed-' || r.chave
              );
            CONTINUE;
        END IF;

        INSERT INTO pergunta (
            enunciado, testamento, nivel, referencia, ativa, criada_em,
            status, categoria, explicacao, chave_seed
        )
        VALUES (
            r.enunciado, r.testamento, r.nivel, r.referencia, true, NOW(),
            'APROVADA', r.categoria, r.explicacao, 'seed-' || r.chave
        )
        RETURNING id INTO novo_id;

        INSERT INTO alternativa (pergunta_id, texto, correta, ordem)
        SELECT novo_id, a.texto, a.correta, a.ordem
        FROM seed_alternativa a
        WHERE a.chave = r.chave;
    END LOOP;
END $$;

DROP TABLE IF EXISTS seed_alternativa;
DROP TABLE IF EXISTS seed_pergunta;
""")
destino.write_text("\n".join(linhas), encoding="utf-8")
print(f"{len(P)} perguntas em {destino}")
