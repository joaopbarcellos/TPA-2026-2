import matplotlib.pyplot as plt
import numpy as np


fig, axs = plt.subplots(3, 2, figsize=(10, 15))

x = ["50k", "100k", "200k", "400k"]
leitura_nord_y = [38.921, 64.296, 108.892, 175.572 ]
leitura_ord_y = [37525.047, 197582.096, 1420921.487, 3688157.834]
pesquisat_nord_y = [3.698, 4.825, 6.436, 10.542]
pesquisat_ord_y = [1.186, 1.590, 24.202, 9.040]
pesquisan_nord_y = [2.551, 3.443, 8.610, 10.754]
pesquisan_ord_y = [1.036, 1.349, 16.083, 5.245]
remocaot_nord_y = [4.522, 2.207, 6.146, 10.549]
remocaot_ord_y = [1.265, 2.222, 19.165, 6.766]
remocaon_nord_y = [3.148, 2.878, 5.815, 6.604]
remocaon_ord_y = [6.056, 6.877, 21.592, 23.277]

axs[0, 0].plot(x, leitura_nord_y, label="Não ordenada", color="blue")
axs[0, 0].plot(x, leitura_ord_y, label="Ordenada", color="red")
axs[0, 0].set_title("Leitura e Montagem da Lista")
axs[0, 0].set_xlabel("Número de contatos")
axs[0, 0].set_ylabel("Tempo (em milissegundos)")
axs[0, 0].legend()

axs[0, 1].plot(x, pesquisat_nord_y, label="Não ordenada", color="blue")
axs[0, 1].plot(x, pesquisat_ord_y, label="Ordenada", color="red")
axs[0, 1].set_title("Pesquisa por Telefone")
axs[0, 1].set_xlabel("Número de contatos")
axs[0, 1].set_ylabel("Tempo (em milissegundos)")
axs[0, 1].legend()

axs[1, 0].plot(x, pesquisan_nord_y, label="Não ordenada", color="blue")
axs[1, 0].plot(x, pesquisan_ord_y, label="Ordenada", color="red")
axs[1, 0].set_title("Pesquisa por Nome")
axs[1, 0].set_xlabel("Número de contatos")
axs[1, 0].set_ylabel("Tempo (em milissegundos)")
axs[1, 0].legend()

axs[1, 1].plot(x, remocaot_nord_y, label="Não ordenada", color="blue")
axs[1, 1].plot(x, remocaot_ord_y, label="Ordenada", color="red")
axs[1, 1].set_title("Remoção por Telefone")
axs[1, 1].set_xlabel("Número de contatos")
axs[1, 1].set_ylabel("Tempo (em milissegundos)")
axs[1, 1].legend()

axs[2, 0].plot(x, remocaon_nord_y, label="Não ordenada", color="blue")
axs[2, 0].plot(x, remocaon_ord_y, label="Ordenada", color="red")
axs[2, 0].set_title("Remoção por Nome")
axs[2, 0].set_xlabel("Número de contatos")
axs[2, 0].set_ylabel("Tempo (em milissegundos)")
axs[2, 0].legend()


axs[2, 1].axis('off')
fig.subplots_adjust(hspace=0.8, wspace=0.5)
plt.show()