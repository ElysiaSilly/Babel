package com.elysiasilly.babel.api.client.screen.old;

public class BabelCursor {
    /*
    public void renderMouse(GuiGraphics guiGraphics, Vec2 mousePos) {
        if(mousePointerTexture() == null) return;
        try
        {
            //GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
            // todo : is there a proper way to render cursors?

            BufferedImage bufferedImage = ImageIO.read(minecraft.getResourceManager().open(Objects.requireNonNull(mousePointerTexture())));


            ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight());

            if(bufferedImage.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
                final BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
                final Graphics2D graphics = convertedImage.createGraphics();
                final int targetWidth = bufferedImage.getWidth();
                final int targetHeight = bufferedImage.getHeight();
                graphics.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
                graphics.dispose();
                bufferedImage = convertedImage;
            }


            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                for (int j = 0; j < bufferedImage.getWidth(); j++) {
                    int colorSpace = bufferedImage.getRGB(j, i);
                    buffer.put((byte) ((colorSpace << 8) >> 24));
                    buffer.put((byte) ((colorSpace << 16) >> 24));
                    buffer.put((byte) ((colorSpace << 24) >> 24));
                    buffer.put((byte) (colorSpace >> 24));
                }
            }


            ;
            GLFWImage image = imageToGLFWImage(bufferedImage);
            //image.set(image.width(), image.height(), buffer);

            System.out.println(image);

            long cursor = GLFW.glfwCreateCursor(image, 0, 16);
            GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //guiGraphics.blitSprite(Objects.requireNonNull(mousePointerTexture()), (int) mousePos.x - 4, (int) mousePos.y - 2, 16, 16);
    }


    private static GLFWImage imageToGLFWImage(BufferedImage image) {

        if (image.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
            final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            final Graphics2D graphics = convertedImage.createGraphics();
            final int targetWidth = image.getWidth();
            final int targetHeight = image.getHeight();
            graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
            graphics.dispose();
            image = convertedImage;
        }

        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int colorSpace = image.getRGB(j, i);
                buffer.put((byte) ((colorSpace << 8) >> 24));
                buffer.put((byte) ((colorSpace << 16) >> 24));
                buffer.put((byte) ((colorSpace << 24) >> 24));
                buffer.put((byte) (colorSpace >> 24));
            }
        }

        buffer.flip();
        final GLFWImage result = GLFWImage.create();
        result.set(image.getWidth(), image.getHeight(), buffer);
        return result;
    }
    */
}
