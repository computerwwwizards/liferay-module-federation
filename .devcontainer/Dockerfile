

FROM ubuntu:latest



ENV SHELL=/bin/zsh

RUN apt-get update && apt-get install -y \
    zip \
    unzip \
    git \
    curl \
    zsh \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

ENV PORT=3000

USER ubuntu

WORKDIR /home/ubuntu

# Install Oh My Zsh for zsh (non-interactive)
RUN sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" --unattended
RUN curl -fsSL https://get.pnpm.io/install.sh | sh 
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
RUN curl -fsSL https://fnm.vercel.app/install | zsh
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source /home/ubuntu/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.402-zulu"
RUN bash -c "source /home/ubuntu/.sdkman/bin/sdkman-init.sh && sdk install jbang && jbang app install jpm@codejive && curl -L https://raw.githubusercontent.com/liferay/liferay-blade-cli/master/cli/installers/local | sh"
RUN echo 'export PATH="$PATH:$HOME/jpm/bin"' >> ~/.zshrc
RUN echo 'export PATH="$PATH:$HOME/jpm/bin"' >> ~/.bash_profile



CMD ["tail", "-f", "/dev/null"]