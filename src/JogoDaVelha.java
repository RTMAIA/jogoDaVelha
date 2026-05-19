import java.util.Random;
import java.util.Scanner;

public class JogoDaVelha {
    String[][] tabuleiro = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};

    public void imprimeTabuleiro() {
        System.out.println(String.format("""
                    1   2    3                
                1   %s | %s | %s 
                  ----|---|----
                2   %s | %s | %s 
                  ----|---|----
                3   %s | %s | %s   
                """, tabuleiro[0][0], tabuleiro[0][1], tabuleiro[0][2], tabuleiro[1][0], tabuleiro[1][1], tabuleiro[1][2], tabuleiro[2][0], tabuleiro[2][1], tabuleiro[2][2]));
    }

//Bloco de verificacao de vitória
    // Bloco de jogo

    public int verificarLinha(int x, String simb) {
        int cont = 0;
        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[x - 1][i].equals(simb)) {
                cont += 1;
            }
        }
        return cont;
    }

    public int verificarColuna(int y, String simb) {
        int cont = 0;
        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][y - 1].equals(simb)) {
                cont += 1;
            }
        }
        return cont;

    }

    public int verificarDiagonalPrincipal(String simb) {
        int cont = 0;
        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][i].equals(simb)) {
                cont += 1;
            }
        }
        return cont;
    }

    public int verificarDiagonalSecundaria(int x, String simb) {
        int cont = 0;
        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][tabuleiro.length - 1 - i].equals(simb)) {
                cont += 1;
            }
        }
        return cont;
    }

//Bloco de jogabilidade

    public int marcarPosicao(int x, int y, String simb) {
        if (tabuleiro[x - 1][y - 1].equals(" ")) {
            tabuleiro[x - 1][y - 1] = simb;
        } else {
            System.out.println("Você não pode preencher onde já esta preenchido.\n");
            return 1;
        }
        return 0;
    }

    public String[] escolherSimbolo() {
        String[] simbolos = {"x", "o"};
        String[] simbJogadores = new String[2];
        int nSimb;
        Random random = new Random();

        nSimb = random.nextInt(0, 2);
        simbJogadores[0] = simbolos[nSimb];
        if (simbJogadores[0].equals(simbolos[0])) {
            simbJogadores[1] = simbolos[1];
        }else {
            simbJogadores[1] = simbolos[0];
        }
        return simbJogadores;
    }

    public String[] aleatorizarJogador(String pJogador, String sJogador) {
        String[] jogadores = {pJogador, sJogador};
        String[] jogadoresAleatorios = new String[2];
        Random random = new Random();
        int nJogador;

        nJogador = random.nextInt(0, 2);
        jogadoresAleatorios[0] = jogadores[nJogador];
        if (jogadoresAleatorios[0].equals(jogadores[0])) {
            jogadoresAleatorios[1] = jogadores[1];
        }else {
            jogadoresAleatorios[1] = jogadores[0];
        }
        return jogadoresAleatorios;
    }

    public int entrada(int x, int y, String simbolo) {
        int saidaMarcacao = 0;
        saidaMarcacao = marcarPosicao(x, y, simbolo);
        imprimeTabuleiro();
        return saidaMarcacao;
    }

    public void empate() {
        int cont = 0;
        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            for  (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[i][j] != " ") {
                    cont  += 1;
                    if (cont == 9) {
                        System.out.println("Empate!");
                        return;
                    }
                }
            }
        }
    }

    public String vitorias(int x, int y, String simbolo, String jogador) {

        if (verificarLinha(x, simbolo) == 3) {
            return jogador + " venceu!";
        }
        if (verificarColuna(y, simbolo) == 3) {
            return jogador + " venceu!";
        }
        if (verificarDiagonalPrincipal(simbolo) == 3) {
            return jogador + " venceu!";
        }
        if (verificarDiagonalSecundaria(x, simbolo) == 3) {
            return jogador + " venceu!";
        }
        return "";
    }

//Bloco de jogadores
    public int[] jogador() {
        int x, y;
        int[] posicoes = {0, 0};

        Scanner teclado = new Scanner(System.in);

        System.out.print("Digite o valor da linha: ");
        x = teclado.nextInt();
        if (x >= 0 && x <= 3) {
            posicoes[0] = x;
            System.out.print("Digite o valor da coluna: ");
            y = teclado.nextInt();
            if (y >= 0 && y <= 3) {
                posicoes[1] = y;
                return posicoes;
            }
        }
        return new  int[0];
    }

//Bloco computador
    public int[] computador(String simb) {
        int[] defesa = defesa(simb);
        int[] ataque = ataque(simb);

        if (defesa.length != 0) {
            return defesa;
        }
        if (ataque.length != 0) {
            return ataque;
        }
        return new int[0];
    }

    //Bloco ofensivo basico
    public int verificaMalha() {
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[i][j].equals(" ")) {
                    cont += 1;
                }
                if (cont == 9) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public int[] marcacaoInicial() {
        int[] posicoes = new int[2];

        if (tabuleiro[1][1].equals(" ")) {
            posicoes[0] = 2;
            posicoes[1] = 2;
            return posicoes;
        }
        if (tabuleiro[0][0].equals(" ")) {
            posicoes[0] = 1;
            posicoes[1] = 1;
            return posicoes;
        }
        if (tabuleiro[0][2].equals(" ")) {
            posicoes[0] = 1;
            posicoes[1] = 3;
            return posicoes;
        }
        if (tabuleiro[2][0].equals(" ")) {
            posicoes[0] = 3;
            posicoes[1] = 1;
            return posicoes;
        }
        if (tabuleiro[2][2].equals(" ")) {
            posicoes[0] = 3;
            posicoes[1] = 3;
            return posicoes;
        }
        return new int[0];
    }

    public int[] ataque(String simb) {
        int[] lacunaLin, lacunaCol, lacunaDiagonalP, lacunaDiagonalS = new int[2];

        lacunaLin = lacunaLinha(simb);
        lacunaCol = lacunaColuna(simb);
        lacunaDiagonalP = lacunaDiagonalPrincipal(simb);
        lacunaDiagonalS = lacunaDiagonalSecundaria(simb);

        if (lacunaLin.length != 0) {
            return lacunaLin;
        }
        if (lacunaCol.length != 0) {
            return lacunaCol;
        }
        if (lacunaDiagonalP.length != 0) {
            return lacunaDiagonalP;
        }
        if (lacunaDiagonalS.length != 0 ) {
            return lacunaDiagonalS;
        }
        return new int[0];
    }

    public int[] defesa(String simb) {
        int[] lacunaLin, lacunaCol, lacunaDiagonalP, lacunaDiagonalS, marcacaoInicial, verificaVitoria = new int[2];
        String simbAdversario = retornaSimboloAdversario(simb);

        lacunaLin = lacunaLinha(simbAdversario);
        lacunaCol = lacunaColuna(simbAdversario);
        lacunaDiagonalP = lacunaDiagonalPrincipal(simbAdversario);
        lacunaDiagonalS = lacunaDiagonalSecundaria(simbAdversario);
        marcacaoInicial = marcacaoInicial();

        if (lacunaLin.length != 0) {
            return lacunaLin;
        }
        if (lacunaCol.length != 0) {
            return lacunaCol;
        }
        if (lacunaDiagonalP.length != 0) {
            return lacunaDiagonalP;
        }
        if (lacunaDiagonalS.length != 0 ) {
            return lacunaDiagonalS;
        }
        return marcacaoInicial;
    }

    // Bloco de defesa basica
    public int[] lacunaDiagonalPrincipal(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++){
            if (tabuleiro[i][i].equals(simb) && tabuleiro[i][i] != " ") {
                cont += 1;
            }
            if (tabuleiro[i][i].equals(" ")) {
                posicoes[0] = i + 1;
                posicoes[1] = i + 1;
            }
            if (cont == 2 && posicoes[0] != 0) {
                return posicoes;
            }
        }
        return  new int[0];
    }

    public int[] lacunaDiagonalSecundaria(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][tabuleiro.length - 1 - i].equals(simb) && tabuleiro[i][tabuleiro.length - 1 - i] != " ") {
                cont += 1;
            }
            if (tabuleiro[i][tabuleiro.length - 1 -i].equals(" ")) {
                posicoes[0] = i + 1;
                posicoes[1] = tabuleiro.length - i;
            }
            if (cont == 2 && posicoes[0] != 0) {
                return posicoes;
            }
        }
        return new int[0];
    }

    public int[] lacunaLinha(String simb) {
        // o bloco esta retornado numero aleatorio padronizar para saida [0, 0]
        int cont = 0;
        int[] posicoes = new int[2];

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            cont = 0;
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[i][j].equals(simb) && tabuleiro[i][j] != " ") {
                    cont += 1;
                }
                if (tabuleiro[i][j].equals(" ")) {
                    posicoes[0] = i + 1;
                    posicoes[1] = j + 1;
                }
                }
                if (cont == 2 && posicoes[0] != 0) {
                    return posicoes;
            }
        }
        return new int[0];

    }

    public int[] lacunaColuna(String simb) {
        // o bloco esta retornado numero aleatorio padronizar para saida [0, 0]
        int cont = 0;
        int[] posicoes = new int[2];

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            cont = 0;
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[j][i].equals(simb) && tabuleiro[j][i] != " ") {
                    cont += 1;
                }
                if (tabuleiro[j][i].equals(" ")) {
                    posicoes[0] = j + 1;
                    posicoes[1] = i + 1;
                }
                }
                if (cont == 2 && posicoes[0] != 0) {
                    return posicoes;
            }
        }
        return new int[0];
    }

    public int[] jogadorOuComputador(String jogador, String simb) {
        if (jogador.equals("Computador 1") || jogador.equals("Computador 2")) {
            return computador(simb);
        }else {
            return jogador();
        }
    }

//Bloco de utilirios
    public String retornaSimboloAdversario(String simb) {
        if (simb.equals("x")) {
            return "o";
        }else {
            return "x";
        }
    }

// Bloco de modo de jogo
    public void pvp(String nomeJogadorUm, String nomeJogadorDois, boolean modo) {
        int saidaEntrada;
        int[] saidaJogador;
        String saidaVitoria;
        String[] simbJogadores, jogadores = new String[0];

        if (!modo) {
            System.out.println("""
                    
                    
                    
                    ██▄  ▄██  ▄▄▄  ▄▄▄▄   ▄▄▄    █████▄ ▄▄ ▄▄ █████▄\s
                    ██ ▀▀ ██ ██▀██ ██▀██ ██▀██   ██▄▄█▀ ██▄██ ██▄▄█▀\s
                    ██    ██ ▀███▀ ████▀ ▀███▀   ██      ▀█▀  ██    \s
                    
                    
                    """);
        }

        jogadores = aleatorizarJogador(nomeJogadorUm, nomeJogadorDois);

        simbJogadores = escolherSimbolo();
        System.out.println(jogadores[0] + " seu simbolo é " + simbJogadores[0].toUpperCase() + "\n" + jogadores[1] + " seu simbolo é " + simbJogadores[1].toUpperCase() + ".\n");

        imprimeTabuleiro();

        System.out.println(jogadores[0] + " começa.\n");

        for (int i = 0; i <= (tabuleiro.length * tabuleiro[0].length) - 1; i++) {
            if (i % 2 == 0) {
                System.out.println(jogadores[0] + "\n");
                saidaJogador = jogadorOuComputador(jogadores[0], simbJogadores[0]);
                if (saidaJogador.length > 0) {
                    saidaEntrada = entrada(saidaJogador[0], saidaJogador[1], simbJogadores[0]);
                    if (saidaEntrada == 0) {
                        saidaVitoria = vitorias(saidaJogador[0], saidaJogador[1], simbJogadores[0], jogadores[0]);
                        if (saidaVitoria != "") {
                            System.out.println(saidaVitoria);
                            return;
                        } else {
                            empate();
                        }
                    } else {
                        i -= saidaEntrada;
                    }
                } else {
                    System.out.println("O valores devem ser apenas os dispostos no tabuleiro.");
                    i -= 1;
                }
            } else {
                System.out.println(jogadores[1] + "\n");
                saidaJogador = jogadorOuComputador(jogadores[1], simbJogadores[1]);
                if (saidaJogador.length > 0) {
                    saidaEntrada = entrada(saidaJogador[0], saidaJogador[1], simbJogadores[1]);
                    if (saidaEntrada == 0) {
                        saidaVitoria = vitorias(saidaJogador[0], saidaJogador[1], simbJogadores[1], jogadores[1]);
                        if (saidaVitoria != "") {
                            System.out.println(saidaVitoria);
                            return;
                        }else {
                            empate();
                        }
                    } else {
                        i -= saidaEntrada;
                    }
                } else {
                    System.out.println("O valores devem ser apenas os dispostos no tabuleiro.");
                    i -= 1;
                }
            }
        }
    }

    public void pvc() {
        System.out.println("""
                
                
                ██▄  ▄██  ▄▄▄  ▄▄▄▄   ▄▄▄    █████▄ ▄▄ ▄▄ ▄█████\s
                ██ ▀▀ ██ ██▀██ ██▀██ ██▀██   ██▄▄█▀ ██▄██ ██    \s
                ██    ██ ▀███▀ ████▀ ▀███▀   ██      ▀█▀  ▀█████\s
                
                """);
        pvp("jogador 1", "Computador 1", true);
    }

    public void cvc() {
        System.out.println("""
                
                
                ██▄  ▄██  ▄▄▄  ▄▄▄▄   ▄▄▄    ▄█████ ▄▄ ▄▄ ▄█████\s
                ██ ▀▀ ██ ██▀██ ██▀██ ██▀██   ██     ██▄██ ██    \s
                ██    ██ ▀███▀ ████▀ ▀███▀   ▀█████  ▀█▀  ▀█████\s
                
                """);
        pvp("Computador 1", "Computador 2", true);
    }

//Bloco principal
    public void jogo() {
        int escolha;
        Scanner teclado = new Scanner(System.in);

        System.out.println("""
                
                ░▀▀█░█▀█░█▀▀░█▀█░░░█▀▄░█▀█░░░█░█░█▀▀░█░░░█░█░█▀█
                ░░░█░█░█░█░█░█░█░░░█░█░█▀█░░░▀▄▀░█▀▀░█░░░█▀█░█▀█
                ░▀▀░░▀▀▀░▀▀▀░▀▀▀░░░▀▀░░▀░▀░░░░▀░░▀▀▀░▀▀▀░▀░▀░▀░▀
                
                ------------------------------------------------------
                |                                                    |
                |  1 - Jogar PvP (Player vs Player)                  |
                |  2 - Jogar PvC (Player vs Computador)              |
                |  3 - Jogar CvC (Computador vs Computador)          |
                |                                                    |
                ------------------------------------------------------""");

        System.out.print("Escolha um modo de jogo: ");
        escolha = teclado.nextInt();
        switch (escolha) {
            case 1: pvp("Jogador 1", "Jogador 2", false);
            break;
            case 2: pvc();
            break;
            case 3: cvc();
            break;
        }


    }
}