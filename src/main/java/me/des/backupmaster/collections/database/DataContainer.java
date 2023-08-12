package me.des.backupmaster.collections.database;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface DataContainer {

    /**
     * Fetches a template from the container.
     *
     * @param name The name of the template
     * @return A future which will be completed with the template, or null if it does not exist
     */
    CompletableFuture<World> fetchWorld(String name);

    /**
     * Saves a template to the container.
     *
     * @param name The name of the template
     * @return A future which will be completed when the save is done
     */
    CompletableFuture<Boolean> saveWorld(String name);

    /**
     * Deletes a template from the container.
     *
     * @param name The name of the template
     * @return A future which will be completed when the delete is done
     */
    CompletableFuture<Void> deleteWorld(String name);

    /**
     * Flushes the container, saving all pending changes. This method is called automatically when the plugin is disabled, and is expected to be joined on.
     *
     * @return A future which will be completed when the flush is done
     */
    CompletableFuture<Void> flush();

    /**
     * Obtains the name of the container.
     *
     * @return The name of the container
     */
    String getName();

    /**
     * Fetches all templates from the container.
     *
     * @return A future which will be completed with a collection of all templates
     */
    CompletableFuture<Collection<String>> fetchAllWorlds();



    /**
     * Enables the data container given the specified databases file Returns a future which resolves to true if the container was enabled successfully, or false
     * if it was not
     */
    default CompletableFuture<Boolean> enable(ConfigurationSection section) {
        // Some containers may not need to be enabled, such as the file container
        return CompletableFuture.completedFuture(true);
    }

    boolean requiresCredentials();

}
