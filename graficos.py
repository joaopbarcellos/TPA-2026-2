import matplotlib.pyplot as plt
import numpy as np


fig, axs = plt.subplots(3, 2, figsize=(10, 15))

x = ["50k", "100k", "200k", "400k"]
leitura_nord_y = [24538.896, 157297.228, 603433.732, 2713562.534 ]
leitura_ord_y = [62688.964, 264689.719, 1143452.664, 5252125.068]
pesquisat_nord_y = [6.405, 17.867, 20.238, 36.146]
pesquisat_ord_y = [2.266, 3.084, 3.593, 4.076]
pesquisan_nord_y = [0.149, 0.472, 0.572, 0.638]
pesquisan_ord_y = [0.097, 0.171, 0.066, 0.134]
remocaot_nord_y = [3.261, 14.051, 17.217, 20.296]
remocaot_ord_y = [0.393, 0.100, 0.100, 0.132]
# remocaon_nord_y = [3.148, 2.878, 5.815, 6.604]
# remocaon_ord_y = [6.056, 6.877, 21.592, 23.277]

axs[0, 0].plot(x, leitura_nord_y, label="Árvore Binária", color="blue")
axs[0, 0].plot(x, leitura_ord_y, label="Árvore AVL", color="red")
axs[0, 0].set_title("Leitura e Montagem da Lista")
axs[0, 0].set_xlabel("Número de contatos")
axs[0, 0].set_ylabel("Tempo (em milissegundos)")
axs[0, 0].legend()

axs[0, 1].plot(x, pesquisat_nord_y, label="Árvore Binária", color="blue")
axs[0, 1].plot(x, pesquisat_ord_y, label="Árvore AVL", color="red")
axs[0, 1].set_title("Pesquisa por Telefone")
axs[0, 1].set_xlabel("Número de contatos")
axs[0, 1].set_ylabel("Tempo (em milissegundos)")
axs[0, 1].legend()

axs[1, 0].plot(x, pesquisan_nord_y, label="Árvore Binária", color="blue")
axs[1, 0].plot(x, pesquisan_ord_y, label="Árvore AVL", color="red")
axs[1, 0].set_title("Pesquisa por Nome")
axs[1, 0].set_xlabel("Número de contatos")
axs[1, 0].set_ylabel("Tempo (em milissegundos)")
axs[1, 0].legend()

axs[1, 1].plot(x, remocaot_nord_y, label="Árvore Binária", color="blue")
axs[1, 1].plot(x, remocaot_ord_y, label="Árvore AVL", color="red")
axs[1, 1].set_title("Remoção por Telefone")
axs[1, 1].set_xlabel("Número de contatos")
axs[1, 1].set_ylabel("Tempo (em milissegundos)")
axs[1, 1].legend()

# axs[2, 0].plot(x, remocaon_nord_y, label="Não ordenada", color="blue")
# axs[2, 0].plot(x, remocaon_ord_y, label="Ordenada", color="red")
# axs[2, 0].set_title("Remoção por Nome")
# axs[2, 0].set_xlabel("Número de contatos")
# axs[2, 0].set_ylabel("Tempo (em milissegundos)")
# axs[2, 0].legend()


axs[2, 1].axis('off')
fig.subplots_adjust(hspace=0.8, wspace=0.5)
plt.show()