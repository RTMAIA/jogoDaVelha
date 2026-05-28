import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClienteFraco {
    static String[][] tabuleiro;
    static Random random = new Random();

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
                    System.out.println("Minha Fraca: " + "(" + simbolo + ")");
                    imprimeTabuleiro(tabuleiro);
                    out.writeObject(tabuleiro);
                    out.flush();
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void preencherMatriz(String[][] matriz, String simb) {
        int[] pos = computador(simb);
        if (pos.length > 0) {
            matriz[pos[0]-1][pos[1]-1] = simb;
        }
    }

    // IA fraca: só ataque básico > defesa básica > aleatório
    static int[] computador(String simb) {
        // Ataque básico
        int[] a = lacunaLinha(simb);   if (a.length != 0) return a;
        a = lacunaColuna(simb);        if (a.length != 0) return a;
        // Defesa básica
        String adv = simb.equals("x") ? "o" : "x";
        a = lacunaLinha(adv);          if (a.length != 0) return a;
        a = lacunaColuna(adv);         if (a.length != 0) return a;
        // Aleatório
        return aleatorio();
    }

    static int[] aleatorio() {
        int[] livres = new int[9];
        int n = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (tabuleiro[i][j].equals(" "))
                    livres[n++] = i*3+j;
        if (n == 0) return new int[0];
        int escolha = livres[random.nextInt(n)];
        return new int[]{escolha/3+1, escolha%3+1};
    }

    static int[] lacunaLinha(String simb) {
        int cont; int[] pos = new int[2];
        for (int i = 0; i < 3; i++) {
            cont = 0; pos = new int[2];
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j].equals(simb)) cont++;
                else if (!tabuleiro[i][j].equals(" ")) cont--;
                else { pos[0]=i+1; pos[1]=j+1; }
            }
            if (cont==2 && pos[0]!=0) return pos;
        }
        return new int[0];
    }

    static int[] lacunaColuna(String simb) {
        int cont; int[] pos = new int[2];
        for (int i = 0; i < 3; i++) {
            cont = 0; pos = new int[2];
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[j][i].equals(simb)) cont++;
                else if (!tabuleiro[j][i].equals(" ")) cont--;
                else { pos[0]=j+1; pos[1]=i+1; }
            }
            if (cont==2 && pos[0]!=0) return pos;
        }
        return new int[0];
    }

    static int verificaTabuleiro() {
        int cont = 0;
        for (String[] row : tabuleiro)
            for (String c : row)
                if (c.equals(" ")) cont++;
        return cont;
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

}