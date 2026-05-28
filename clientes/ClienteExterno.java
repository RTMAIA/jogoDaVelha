import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClienteExterno {
    static String[][] tabuleiro;
    
    public static void main(String[] args) {
        String simbolo;
        Scanner teclado = new Scanner(System.in);
        String host;
        int porta;

        System.out.print("Digite o host da partida: ");
        host = "localhost";
        System.out.print("Digite a porta: ");
        porta = 12345;

        try (Socket socket = new Socket(host, porta);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            simbolo = (String) in.readObject();
            
            while (true) {
                
                tabuleiro = (String[][]) in.readObject();
                
                if (tabuleiro.length == 3) {
                    System.out.println("\nRecebido do servidor: ");
                    imprimeTabuleiro(tabuleiro);
                }

                if (tabuleiro.length == 2) {
                    System.out.println(tabuleiro[1][0]);
                    out.writeObject(null);
                    out.flush();
                    break;
                }
              
                if (verificaTabuleiro() != 0 && tabuleiro.length == 3) {
                    preencherMatriz(tabuleiro, simbolo);
                    System.out.println("Enviado para o servidor: ");
                    System.out.println("Minha AI: " + "(" + simbolo + ")");
                    imprimeTabuleiro(tabuleiro);
                    out.writeObject(tabuleiro);
                    out.flush();
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void imprimeTabuleiro(String[][] matriz) {
        System.out.println(String.format("""
                    1   2    3                
                1   %s | %s | %s 
                  ----|---|----
                2   %s | %s | %s 
                  ----|---|----
                3   %s | %s | %s   
                """, tabuleiro[0][0], tabuleiro[0][1], tabuleiro[0][2], tabuleiro[1][0], tabuleiro[1][1], tabuleiro[1][2], tabuleiro[2][0], tabuleiro[2][1], tabuleiro[2][2]));
    }

    public static void preencherMatriz(String[][] matriz, String valor) {
        int[] posicoes = computador(valor);
        if (posicoes.length > 0) {
            matriz[posicoes[0] - 1][posicoes[1] - 1] = valor;
        }
        }

    public static int verificaTabuleiro() {
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[i][j].equals(" ")) {
                    cont += 1;
                }
                if (cont == 9) {
                    return cont;
                }
            }
        }
        return cont;
}

    public static int[] computador(String simb) {
        int[] defesa = defesa(simb);
        int[] ataque = ataque(simb);

        if (ataque.length != 0) {
            return ataque;
        }
        if (defesa.length != 0) {
            return defesa;
        }
        return new int[0];
    }

    public static int[] marcacaoInicial() {
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

    public static int[] posicoesFinais() {
        int[] posicoes = new int[2];

        if (tabuleiro[0][1].equals(" ")) {
            posicoes[0] = 1;
            posicoes[1] = 2;
            return posicoes;
        }
        if (tabuleiro[1][0].equals(" ")) {
            posicoes[0] = 2;
            posicoes[1] = 1;
            return posicoes;
        }
        if (tabuleiro[2][1].equals(" ")) {
            posicoes[0] = 3;
            posicoes[1] = 2;
            return posicoes;
        }
        if (tabuleiro[1][2].equals(" ")) {
            posicoes[0] = 2;
            posicoes[1] = 3;
            return posicoes;
        }
        return new int[0];
    }

    public static int[] lacunaLinha(String simb) {
        // o bloco esta retornado numero aleatorio padronizar para saida [0, 0]
        int cont = 0;
        int[] posicoes = new int[2];

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            cont = 0;
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[i][j].equals(simb)) {
                    cont += 1;
                } else {
                    if (!tabuleiro[i][j].equals(" ")) {
                        cont -= 1;
                    } else {
                        posicoes[0] = i + 1;
                        posicoes[1] = j + 1;
                    }
                }
            }
            if (cont == 2 && posicoes[0] != 0) {
                return posicoes;
            }
        }
        return new int[0];
    }

    public static int[] lacunaColuna(String simb) {
        // o bloco esta retornado numero aleatorio padronizar para saida [0, 0]
        int cont = 0;
        int[] posicoes = new int[2];

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            cont = 0;
            for (int j = 0; j <= tabuleiro.length - 1; j++) {
                if (tabuleiro[j][i].equals(simb)) {
                    cont += 1;
                } else {
                    if (!tabuleiro[j][i].equals(" ")) {
                        cont -= 1;
                    } else {
                        posicoes[0] = j + 1;
                        posicoes[1] = i + 1;
                    }
                }
            }
            if (cont == 2 && posicoes[0] != 0) {
                return posicoes;
            }
        }
        return new int[0];
    }

    public static int[] lacunaDiagonalPrincipal(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++){
            if (tabuleiro[i][i].equals(simb) && !tabuleiro[i][i].equals(" ")) {
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

    public static int[] lacunaDiagonalSecundaria(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][tabuleiro.length - 1 - i].equals(simb) && !tabuleiro[i][tabuleiro.length - 1 - i].equals(" ")) {
                cont += 1;
            }
            if (tabuleiro[i][tabuleiro.length - 1 - i].equals(" ")) {
                posicoes[0] = i + 1;
                posicoes[1] = tabuleiro.length - i;
            }
            if (cont == 2 && posicoes[0] != 0) {
                return posicoes;
            }
        }
        return new int[0];
    }

    // Bloco ofensivo basico
    public static int[] ataque(String simb) {
        int[] lacunaLin, lacunaCol, lacunaDiagonalP, lacunaDiagonalS, ataqueGarfoDiagonalP, ataqueGarfoDiagonalS = new int[2];

        lacunaLin = lacunaLinha(simb);
        lacunaCol = lacunaColuna(simb);
        lacunaDiagonalP = lacunaDiagonalPrincipal(simb);
        lacunaDiagonalS = lacunaDiagonalSecundaria(simb);
        ataqueGarfoDiagonalP = ataqueGarfoDiagonalPrincipal(simb);
        ataqueGarfoDiagonalS = ataqueGarfoDiagonalSecundaria(simb);

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
        if (ataqueGarfoDiagonalP.length != 0) {
            return ataqueGarfoDiagonalP;
        }
        if (ataqueGarfoDiagonalS.length != 0) {
            return ataqueGarfoDiagonalS;
        }
        return new int[0];
    }

    public static int[] ataqueGarfoDiagonalPrincipal(String simb) {
        int[] posicoes = new int[2];
        String simboloAdversario = retornaSimboloAdversario(simb);
        int tabuleiroVazio = verificaTabuleiro();

        if (tabuleiroVazio == 9) {
            posicoes[0] = 1;
            posicoes[1] = 1;
            return posicoes;
        }
        if (tabuleiroVazio == 7 && tabuleiro[0][0].equals(simb) && !tabuleiro[1][1].equals(simb)) {
            posicoes[0] = 3;
            posicoes[1] = 3;
            if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                return posicoes;
            }
        }
        if (tabuleiroVazio == 7 && tabuleiro[0][0].equals(simboloAdversario) && tabuleiro[1][1].equals(simb) && !tabuleiro[2][2].equals(simb)) {
            if (tabuleiro[0][2].equals(simboloAdversario)) {
                posicoes[0] = 3;
                posicoes[1] = 1;
                if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                    return posicoes;
                }
        } else {
            if (tabuleiro[2][1].equals(simboloAdversario)) {
                posicoes[0] = 1;
                posicoes[1] = 3;
                if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                    return posicoes;
                    }
                }
            }
        }
    return new int[0];
    }

    public static int[] ataqueGarfoDiagonalSecundaria(String simb) {
        int[] posicoes = new int[2];
        String simboloAdversario = retornaSimboloAdversario(simb);
        int tabuleiroVazio = verificaTabuleiro();

        if (tabuleiroVazio == 0) {
            posicoes[0] = 1;
            posicoes[1] = 1;
            return posicoes;
        }

        if (tabuleiroVazio == 7 && tabuleiro[0][2].equals(simb) && tabuleiro[1][1].equals(simboloAdversario)) {
            posicoes[0] = 3;
            posicoes[1] = 1;
            if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                return posicoes;
            }
        }

        if (tabuleiro[0][2].equals(simboloAdversario) && tabuleiro[1][1].equals(simb) && tabuleiro[2][0].equals(simboloAdversario)) {
            if (tabuleiro[2][2].equals(simboloAdversario)) {
                posicoes[0] = 1;
                posicoes[1] = 1;
                if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                    return posicoes;
                }
            } else {
                if (tabuleiro[0][0].equals(simboloAdversario)) {
                    posicoes[0] = 2;
                    posicoes[1] = 2;
                    if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                        return posicoes;
                    }
                    }
                }
            }
        return new int[0];
    }

    // Bloco de defesa basica
    public static int[] defesa(String simb) {
        int[] lacunaLin, lacunaCol, lacunaDiagonalP, lacunaDiagonalS, marcacaoInicial,lacunaGarfoDiagonaloP, lacunaGarfoDiagonaloS, lacunaGarfoLinCol, posicaoFinal = new int[2];
        String simbAdversario = retornaSimboloAdversario(simb);

        lacunaLin = lacunaLinha(simbAdversario);
        lacunaCol = lacunaColuna(simbAdversario);
        lacunaDiagonalP = lacunaDiagonalPrincipal(simbAdversario);
        lacunaDiagonalS = lacunaDiagonalSecundaria(simbAdversario);
        lacunaGarfoDiagonaloP = lacunaGarfoDiagonaloPrincipal(simb);
        lacunaGarfoDiagonaloS = lacunaGarfoDiagonaloSecundaria(simb);
        lacunaGarfoLinCol = lacunaGarfoLinhaColuna(simb);
        marcacaoInicial = marcacaoInicial();
        posicaoFinal = posicoesFinais();

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
        if (lacunaGarfoDiagonaloP.length != 0) {
            return lacunaGarfoDiagonaloP;
        }
        if (lacunaGarfoDiagonaloS.length != 0) {
            return lacunaGarfoDiagonaloS;
        }
        if (lacunaGarfoLinCol.length != 0) {
            return lacunaGarfoLinCol;
        }
        if (marcacaoInicial.length != 0) {
            return marcacaoInicial;
        }
        return posicaoFinal;
    }

    public static int[] lacunaGarfoLinhaColuna(String simb) {
        int[] posicoes = new int[2];
        String simboloAdversario = retornaSimboloAdversario(simb);

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (i % 2 == 0) {
                if (tabuleiro[0][i].equals(simboloAdversario) && tabuleiro[1][1].equals(simb) && tabuleiro[2][1].equals(simboloAdversario)) {
                    posicoes[0] = 2;
                    posicoes[1] = 1;
                    if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                        return posicoes;
                    }
                }
            }
        }
        return new int[0];
    }

    public static int[] lacunaGarfoDiagonaloPrincipal(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;
        String simboloAdversario = retornaSimboloAdversario(simb);

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][i].equals(simboloAdversario)) {
                cont += 1;
            }
            if (cont == 1 && tabuleiro[i][i].equals(simb)) {
                posicoes[0] = i + 1;
                posicoes[1] = i;
                cont += 1;
            }
            if (cont == 3) {
                if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                    return posicoes;
                }
            }
        }
        return new int[0];
    }

    public static int[] lacunaGarfoDiagonaloSecundaria(String simb) {
        int[] posicoes = new int[2];
        int cont = 0;
        String simboloAdversario = retornaSimboloAdversario(simb);

        for (int i = 0; i <= tabuleiro.length - 1; i++) {
            if (tabuleiro[i][tabuleiro.length - 1 - i].equals(simboloAdversario)) {
                cont += 1;
            }
            if (cont == 1 && tabuleiro[i][tabuleiro.length - 1 - i].equals(simb)) {
                posicoes[0] = i + 1;
                posicoes[1] = i + 2;
                cont += 1;
            }
            if (cont == 3) {
                if (tabuleiro[posicoes[0] - 1][posicoes[1] - 1].equals(" ")) {
                    return posicoes;
                }
            }
        }
        return new int[0];
    }

    public static String retornaSimboloAdversario(String simb) {
        if (simb.equals("x")) {
            return "o";
        }else {
            return "x";
        }
    }

}