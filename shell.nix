let
  nixpkgsVer = "e6515129584048031d14f4ba61481a2254314018";
  pkgs = import (fetchTarball "https://github.com/NixOS/nixpkgs/archive/${nixpkgsVer}.tar.gz") { config = {}; overlays = []; };
  libs = with pkgs; [
    libpulseaudio
    libGL
    glfw
    openal
    stdenv.cc.cc.lib
  ];
in pkgs.mkShell {
  name = "trickster";

  buildInputs = with pkgs; [
    jdk21
  ] ++ libs;

  LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;
}
