{
  inputs.nixpkgs.url = "github:nixos/nixpkgs";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachSystem [
         "aarch64-darwin"
         "aarch64-linux"
         "x86_64-linux"
         "x86_64-darwin"
       ] (system:
      let
        pkgs = import nixpkgs { inherit system; };
      in
      {
        devShells.default = pkgs.mkShell {
          packages = [
            pkgs.cowsay
            pkgs.coursier
            pkgs.graalvm17-ce
            pkgs.sbt
            pkgs.scala-cli
            pkgs.scalafmt
          ];
        };
      });
}
